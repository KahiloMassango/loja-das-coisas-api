package org.example.loja_das_coisas_api.product.service

import org.example.loja_das_coisas_api.exception.EntityNotFoundException
import org.example.loja_das_coisas_api.product.dto.request.ProductItemDtoReq
import org.example.loja_das_coisas_api.product.dto.request.ProductItemUpdateDtoReq
import org.example.loja_das_coisas_api.product.dto.response.ProductItemDtoRes
import org.example.loja_das_coisas_api.product.dto.response.ProductItemStoreDtoRes
import org.example.loja_das_coisas_api.product.mapper.toDtoRes
import org.example.loja_das_coisas_api.product.mapper.toStoreDtoRes
import org.example.loja_das_coisas_api.product.model.ProductItem
import org.example.loja_das_coisas_api.product.repository.ColorRepository
import org.example.loja_das_coisas_api.product.repository.ProductItemRepository
import org.example.loja_das_coisas_api.product.repository.ProductRepository
import org.example.loja_das_coisas_api.product.repository.SizeRepository
import org.example.loja_das_coisas_api.shared.service.ImageService
import org.springframework.stereotype.Service
import java.util.*
import kotlin.jvm.optionals.getOrNull

@Service
class ProductItemService(
    private val productItemRepository: ProductItemRepository,
    private val productRepository: ProductRepository,
    private val colorRepository: ColorRepository,
    private val sizeRepository: SizeRepository,
    private val imageService: ImageService
) {

    fun create(productId: UUID, request: ProductItemDtoReq): ProductItemStoreDtoRes {
        val product = productRepository.findByIdAndDeletedFalse(productId).get()

        val color = if (product.category.hasColorVariation) {
            colorRepository.findById(request.colorId!!)
                .getOrNull() ?: throw EntityNotFoundException("Cor inválida")
        } else null

        val size = if (product.category.hasSizeVariation) {
            sizeRepository.findById(request.sizeId!!)
                .getOrNull() ?: throw EntityNotFoundException("Tamanho inválido")
        } else null

        val imageUrl = imageService.uploadImage(request.image)

        val productItem = ProductItem(
            product = product,
            stockQuantity = request.stockQuantity,
            price = request.price,
            imageUrl = imageUrl,
            color = color,
            size = size
        )
        return productItemRepository.save(productItem).toStoreDtoRes()
    }

    fun update(productId: UUID, productItemId: UUID, request: ProductItemUpdateDtoReq): ProductItemDtoRes {
        val fetchedProductItem = productItemRepository.findByIdAndDeletedFalse(productItemId)
            .getOrNull() ?: throw EntityNotFoundException("Variação não encontrada")

        if (fetchedProductItem.product.id!! != productId) {
            throw EntityNotFoundException("Variação não encontrada")
        }

        request.image?.let {
            fetchedProductItem.imageUrl = imageService.uploadImage(request.image)
        }

        val updated = fetchedProductItem.apply {
            price = request.price
            stockQuantity = request.stockQuantity
        }

        return productItemRepository.save(updated).toDtoRes()
    }

    fun delete(productId: UUID, productItemId: UUID, storeEmail: String) {
        val product = productRepository.findByIdAndDeletedFalse(productId)
            .getOrNull() ?: throw EntityNotFoundException("Produto não encontrado")

        if (!product.store.active || product.store.user.email != storeEmail) {
            throw EntityNotFoundException("Produto não encontrado")
        }

        val existing = productItemRepository.findByIdAndProductId(productItemId, productId)
            ?: throw EntityNotFoundException("Variação não encontrada")

        existing.deleted = true
        productItemRepository.save(existing)
    }
}
