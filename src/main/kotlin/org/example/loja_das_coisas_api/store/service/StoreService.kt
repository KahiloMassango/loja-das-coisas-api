package org.example.loja_das_coisas_api.store.service

import jakarta.transaction.Transactional
import org.example.loja_das_coisas_api.auth.model.User
import org.example.loja_das_coisas_api.auth.model.UserRole
import org.example.loja_das_coisas_api.auth.repository.UserRepository
import org.example.loja_das_coisas_api.exception.AlreadyExistsException
import org.example.loja_das_coisas_api.exception.EntityNotFoundException
import org.example.loja_das_coisas_api.order.repository.OrderRepository
import org.example.loja_das_coisas_api.product.repository.ProductItemRepository
import org.example.loja_das_coisas_api.product.repository.ProductRepository
import org.example.loja_das_coisas_api.product.repository.util.ProductSpecification
import org.example.loja_das_coisas_api.product.mapper.toDtoRes
import org.example.loja_das_coisas_api.store.dto.StoreAdminDtoRes
import org.example.loja_das_coisas_api.store.dto.StoreDetailDtoRes
import org.example.loja_das_coisas_api.store.dto.StoreDtoRes
import org.example.loja_das_coisas_api.store.dto.StoreRegisterRequest
import org.example.loja_das_coisas_api.store.model.StoreProfile
import org.example.loja_das_coisas_api.store.model.toStoreAdminDtoRes
import org.example.loja_das_coisas_api.store.model.toStoreDetailDtoRes
import org.example.loja_das_coisas_api.store.model.toStoreDtoRes
import org.example.loja_das_coisas_api.store.repository.StoreRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalTime
import java.util.*
import kotlin.jvm.optionals.getOrNull

@Service
class StoreService(
    private val storeRepository: StoreRepository,
    private val productRepository: ProductRepository,
    private val productItemRepository: ProductItemRepository,
    private val orderRepository: OrderRepository,
    private val encoder: PasswordEncoder,
    private val userRepository: UserRepository,
) {

    @Transactional
    fun createStore(request: StoreRegisterRequest): StoreDtoRes {
        if (userRepository.findByEmailOrPhoneNumberAndDeletedFalse(request.email, request.phoneNumber) != null) {
            throw AlreadyExistsException("Email ou número de telemóvel já em uso!")
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
            StoreProfile(
                storeName = request.storeName,
                storeLogoUrl = request.storeLogoUrl,
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

    fun getStoreById(id: UUID): StoreDetailDtoRes {
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

    fun updateStore(email: String, request: StoreRegisterRequest): StoreDtoRes {
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

    fun updateStoreStatus(storeId: UUID, isActive: Boolean) {
        val store = storeRepository.findById(storeId)
        if (!store.isPresent) {
            throw EntityNotFoundException("Store not found")
        }
        store.get().active = isActive
        storeRepository.save(store.get())
    }

    fun deleteStore(email: String) {
        val store = storeRepository.findByEmail(email)!!
        store.apply {
            deleted = true
            active = false
        }
        storeRepository.save(store)
    }

    fun getAllStores(): List<StoreAdminDtoRes> {
        return storeRepository.findAll().map { it.toStoreAdminDtoRes() }
    }

    @Transactional
    fun storeDetails(email: String): StoreDetailDtoRes {
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
