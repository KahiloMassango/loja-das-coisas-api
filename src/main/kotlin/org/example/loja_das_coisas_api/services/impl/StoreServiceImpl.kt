package org.example.loja_das_coisas_api.services.impl

import jakarta.transaction.Transactional
import org.example.loja_das_coisas_api.dtos.requests.StoreDtoReq
import org.example.loja_das_coisas_api.dtos.responses.StoreAdminDtoRes
import org.example.loja_das_coisas_api.dtos.responses.StoreDetailDtoRes
import org.example.loja_das_coisas_api.dtos.responses.StoreDtoRes
import org.example.loja_das_coisas_api.exceptions.AlreadyExistsException
import org.example.loja_das_coisas_api.exceptions.EntityNotFoundException
import org.example.loja_das_coisas_api.models.User
import org.example.loja_das_coisas_api.models.UserRole
import org.example.loja_das_coisas_api.models.mappers.toDtoRes
import org.example.loja_das_coisas_api.models.mappers.toStoreAdminDtoRes
import org.example.loja_das_coisas_api.models.mappers.toStoreDetailDtoRes
import org.example.loja_das_coisas_api.models.mappers.toStoreDtoRes
import org.example.loja_das_coisas_api.models.store.Store
import org.example.loja_das_coisas_api.repositories.*
import org.example.loja_das_coisas_api.repositories.util.ProductSpecification
import org.example.loja_das_coisas_api.services.interfaces.StoreService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalTime
import java.util.*
import kotlin.jvm.optionals.getOrNull

@Service
class StoreServiceImpl(
    private val storeRepository: StoreRepository,
    private val productRepository: ProductRepository,
    private val productItemRepository: ProductItemRepository,
    private val orderRepository: OrderRepository,
    private val encoder: PasswordEncoder,
    private val userRepository: UserRepository,
) : StoreService {

    @Transactional
    override fun createStore(request: StoreDtoReq): StoreDtoRes {
        if (userRepository.findByEmailOrPhoneNumberAndDeletedFalse(request.email, request.phoneNumber) != null) {
            throw AlreadyExistsException("Email o u número de telemóvel já em uso!")
        }

        if (storeRepository.findByStoreNameIsAndDeletedFalse(request.storeName).isPresent) {
            throw AlreadyExistsException("Já existe uma loja com este nome")
        }

        val user = User(
            email = request.email,
            password = encoder.encode(request.password),
            role = UserRole.STORE,
            phoneNumber = request.phoneNumber,
        )
        val userStore = userRepository.saveAndFlush(user)

        val createdStore = storeRepository.save(
            Store(
                storeName = request.storeName,
                storeLogoUrl = request.storeLogoUrl,  // TODO(store image correctly and save url)
                description = request.description,
                nif = request.nif,
                latitude = request.latitude,
                longitude = request.longitude,
                active = true,
                offersDelivery = request.offersDelivery,
                openingTime = LocalTime.parse(request.openingTime),
                closingTime = LocalTime.parse(request.closingTime),
                address = request.address,
                user = userStore,
                bankAccountName = request.bankAccountName,
                bankAccountIban = request.bankAccountIban,
                fee = request.fee,
            )
        )

        return createdStore.toStoreDtoRes()
    }

    override fun getStoreById(id: UUID): StoreDetailDtoRes {
        val store = storeRepository.findById(id)
            .getOrNull() ?: throw EntityNotFoundException("Loja não encontrada")

        val products = productRepository.findAll(ProductSpecification().allActiveStoreProducts(store.id!!))
            .map {
                val minPrice = productItemRepository.findMinPriceByProductId(it.id!!)
                it.toDtoRes(minPrice)
            }

        val totalCompletedOrders = orderRepository.findAllStoreCompletedOrders(store.id!!)

        return store.toStoreDetailDtoRes(
            totalProducts = products.size,
            totalCompletedOrders = totalCompletedOrders,
            products = products
        )
    }

    override fun updateStore(email: String, request: StoreDtoReq): StoreDtoRes {
        val store = storeRepository.findByEmail(email)!!

        store.apply {
            storeName = request.storeName
            storeLogoUrl = request.storeLogoUrl
            description = request.description
            nif = request.nif
            latitude = request.latitude
            longitude = request.longitude
        }

        return storeRepository.save(store).toStoreDtoRes()
    }

    override fun updateStoreStatus(storeId: UUID, isActive: Boolean) {
        val store = storeRepository.findById(storeId)
        if (!store.isPresent) {
            throw EntityNotFoundException("Store not found")
        }
        store.get().active = isActive
        storeRepository.save(store.get())
    }

    override fun deleteStore(email: String) {
        val store = storeRepository.findByEmail(email)!!
        store.apply {
            deleted = true
            active = false
        }
        storeRepository.save(store)
    }

    override fun getAllStores(): List<StoreAdminDtoRes> {
        return storeRepository.findAll().map { it.toStoreAdminDtoRes() }
    }

    @Transactional
    override fun storeDetails(email: String): StoreDetailDtoRes {
        val store = storeRepository.findByEmail(email)!!

        val products = productRepository.findAll(
            ProductSpecification().allActiveStoreProducts(store.id!!)
        ).map {
            val minPrice = productItemRepository.findMinPriceByProductId(it.id!!)
            it.toDtoRes(minPrice)
        }

        return store.toStoreDetailDtoRes(products.size, 5, products)
    }

}
