package org.example.loja_das_coisas_api.services.impl

import org.example.loja_das_coisas_api.dtos.requests.ProductDtoReq
import org.example.loja_das_coisas_api.dtos.requests.ProductFilterDtoReq
import org.example.loja_das_coisas_api.dtos.requests.ProductUpdateDtoReq
import org.example.loja_das_coisas_api.dtos.responses.ProductDtoRes
import org.example.loja_das_coisas_api.dtos.responses.ProductStoreDtoRes
import org.example.loja_das_coisas_api.dtos.responses.ProductWithVariationDtoRes
import org.example.loja_das_coisas_api.dtos.responses.ProductWithVariationStoreDtoRes
import org.example.loja_das_coisas_api.exceptions.AlreadyExistsException
import org.example.loja_das_coisas_api.exceptions.EntityNotFoundException
import org.example.loja_das_coisas_api.models.Product
import org.example.loja_das_coisas_api.models.mappers.toDtoRes
import org.example.loja_das_coisas_api.models.mappers.toDtoResWithVariation
import org.example.loja_das_coisas_api.models.mappers.toStoreDtoRes
import org.example.loja_das_coisas_api.models.mappers.toStoreDtoResWithVariation
import org.example.loja_das_coisas_api.repositories.*
import org.example.loja_das_coisas_api.repositories.util.ProductSpecification
import org.example.loja_das_coisas_api.services.interfaces.ProductService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import kotlin.jvm.optionals.getOrNull

@Service
@Transactional
class ProductServiceImpl(
    private val productRepository: ProductRepository,
    private val productItemRepository: ProductItemRepository,
    private val storeRepository: StoreRepository,
    private val categoryRepository: CategoryRepository,
    private val genderRepository: GenderRepository,
    private val imageService: ImageService,
) : ProductService {

    // Store
    override fun createProduct(storeEmail: String, request: ProductDtoReq): ProductStoreDtoRes {
        if (productRepository.findByNameAndDeletedFalse(request.name).isPresent) {
            throw AlreadyExistsException("Produto com mesmo nome já cadastrado")
        }

        val existingCategory = categoryRepository.findById(request.categoryId)
            .getOrNull() ?: throw EntityNotFoundException("Categoria não encontrado")

        val existingGender = genderRepository.findById(request.genderId)
            .getOrNull() ?: throw EntityNotFoundException("Gênero não encontrado")

        val existingStore = storeRepository.findByEmail(storeEmail)
            ?: throw Exception("Store with id $storeEmail not found")

        val imageUrl = imageService.uploadImage(request.image)

        val product = Product(
            name = request.name,
            description = request.description,
            available = request.isAvailable,
            imageUrl = imageUrl,
            store = existingStore,
            category = existingCategory,
            gender = existingGender
        )

        return productRepository.saveAndFlush(product).toStoreDtoRes()
    }

    override fun getProductByIdAndStoreId(storeEmail: String, productId: UUID): ProductWithVariationStoreDtoRes {
        val fetchedProduct = productRepository.findByIdAndDeletedFalse(productId)
            .getOrNull() ?: throw EntityNotFoundException("Produto não encontrado")

        if (!fetchedProduct.store.active || fetchedProduct.store.user.email != storeEmail) {
            throw EntityNotFoundException("Produto não encontrado")
        }


        val productItems = productItemRepository.findAllByProductIdAndDeletedFalse(fetchedProduct.id!!)
            .map { it.toStoreDtoRes() }

        return fetchedProduct.toStoreDtoResWithVariation(productItems)
    }

    override fun deleteProduct(storeEmail: String, productId: UUID) {
        val fetchedProduct = productRepository.findByIdAndDeletedFalse(productId)
            .getOrNull() ?: throw EntityNotFoundException("Produto não encontrado")

        if (!fetchedProduct.store.active || fetchedProduct.store.user.email != storeEmail) {
            throw EntityNotFoundException("Produto não encontrado")
        }

        // Delete all product items
        productItemRepository.deleteAllByProductId(productId)

        // Delete the actual product
        val product = productRepository.findById(productId).get().apply {
            available = false
            deleted = true
        }
        productRepository.save(product)
    }

    override fun updateProduct(storeEmail: String, productId: UUID, request: ProductUpdateDtoReq): ProductStoreDtoRes {
        val fetchedProduct = productRepository.findByIdAndDeletedFalse(productId)
            .getOrNull() ?: throw EntityNotFoundException("Produto não encontrado")

        if (!fetchedProduct.store.active || fetchedProduct.store.user.email != storeEmail) {
            throw EntityNotFoundException("Produto não encontrado")
        }

        if (request.image != null) {
            fetchedProduct.imageUrl = imageService.uploadImage(request.image)
        }

        val existingProduct = fetchedProduct.apply {
            name = request.name
            description = request.description
            available = request.isAvailable
        }

        return productRepository.save(existingProduct).toStoreDtoRes()
    }

    override fun filterForStore(storeEmail: String, gender: String?, category: String?): List<ProductStoreDtoRes> {
        val store = storeRepository.findByEmail(storeEmail)
             ?: throw EntityNotFoundException("Lojá indisponível")

        val genderId = gender?.let {
            genderRepository.findByNameEqualsAndDeletedFalse(it)
                .getOrNull()?.id ?: throw EntityNotFoundException("Gênero não encontrado")
        }
        val categoryId = category?.let {
            categoryRepository.findByNameIsAndDeletedFalse(it)
                .getOrNull()?.id ?: throw EntityNotFoundException("Categoria não encontrada")
        }

        val products = productRepository.findAll(
            ProductSpecification().filterForStore(store.id!!, genderId, categoryId)
        )

        return products.map { product -> product.toStoreDtoRes() }
    }

    override fun getStoreProducts(storeEmail: String): List<ProductStoreDtoRes> {
        val store = storeRepository.findByEmail(storeEmail)
            ?: throw EntityNotFoundException("Lojá indisponível")

        val products = productRepository.findAllByStoreIdAndDeletedFalse(store.id!!)
        return products.map { it.toStoreDtoRes() }
    }

    // CUSTOMER
    override fun getProductById(id: UUID): ProductWithVariationDtoRes {
        val fetchedProduct = productRepository.findByIdAndAvailableTrueAndDeletedFalse(id)

        // If product doesn't exist or the store is not active throw not found exception
        if (!fetchedProduct.isPresent) {
            throw EntityNotFoundException("Produto não encontrado")
        } else if (!fetchedProduct.get().store.active) {
            throw EntityNotFoundException("Produto não encontrado")
        }

        val productItems = productItemRepository
            .findAllByProductIdAndStockQuantityIsGreaterThanAndDeletedFalse(fetchedProduct.get().id!!)
            .map { it.toDtoRes() }

        if (productItems.isEmpty()) {
            throw EntityNotFoundException("Produto não encontrado")
        }

        return fetchedProduct.get().toDtoResWithVariation(productItems)
    }

    override fun getProductsByFilter(filter: ProductFilterDtoReq): List<ProductDtoRes> {
        TODO("Not yet implemented")
    }

    override fun search(query: String): List<ProductDtoRes> {
        val products = productRepository.findAll(ProductSpecification().search(query))
        return products.map { product ->
            val minPrice = productItemRepository.findMinPriceByProductId(product.id!!)
            product.toDtoRes(minPrice)
        }
    }

    override fun filterProductsByGenderAndCategory(gender: String?, category: String?): List<ProductDtoRes> {
        val genderId = gender?.let {
            genderRepository.findByNameEqualsAndDeletedFalse(it)
                .getOrNull()?.id ?: throw EntityNotFoundException("Gênero não encontrado")
        }
        val categoryId = category?.let {
            categoryRepository.findByNameIsAndDeletedFalse(it)
                .getOrNull()?.id ?: throw EntityNotFoundException("Categoria não encontrada")
        }

        val products = productRepository.findAll(
            ProductSpecification().filter(genderId, categoryId)
        )

        return products.map { product ->
            val minPrice = productItemRepository.findMinPriceByProductId(product.id!!)
            product.toDtoRes(minPrice)
        }
    }

    override fun getProductsByStoreId(storeId: UUID): List<ProductDtoRes> {
        storeRepository.findByIdAndActiveIsTrueAndDeletedFalse(storeId)
            .getOrNull() ?: throw EntityNotFoundException("Lojá indisponível")

        val products = productRepository.findAll(ProductSpecification().allActiveStoreProducts(storeId))

        return products.map {
            val minPrice = productItemRepository.findMinPriceByProductId(it.id!!)
            it.toDtoRes(minPrice)
        }
    }

}
