package org.example.loja_das_coisas_api.services.impl

import org.example.loja_das_coisas_api.dtos.requests.ProductItemDtoReq
import org.example.loja_das_coisas_api.dtos.requests.ProductItemUpdateDtoReq
import org.example.loja_das_coisas_api.dtos.responses.ProductItemDtoRes
import org.example.loja_das_coisas_api.dtos.responses.ProductItemStoreDtoRes
import org.example.loja_das_coisas_api.exceptions.EntityNotFoundException
import org.example.loja_das_coisas_api.models.ProductItem
import org.example.loja_das_coisas_api.models.mappers.toDtoRes
import org.example.loja_das_coisas_api.models.mappers.toStoreDtoRes
import org.example.loja_das_coisas_api.repositories.ColorRepository
import org.example.loja_das_coisas_api.repositories.ProductItemRepository
import org.example.loja_das_coisas_api.repositories.ProductRepository
import org.example.loja_das_coisas_api.repositories.SizeRepository
import org.example.loja_das_coisas_api.services.interfaces.ProductItemService
import org.springframework.stereotype.Service
import java.util.*
import kotlin.jvm.optionals.getOrNull

@Service
class ProductItemServiceImpl(
    private val productItemRepository: ProductItemRepository,
    private val productRepository: ProductRepository,
    private val colorRepository: ColorRepository,
    private val sizeRepository: SizeRepository,
    private val imageService: ImageService
) : ProductItemService {

    override fun create(productId: UUID, request: ProductItemDtoReq): ProductItemStoreDtoRes {

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
            product = product, // **Use the saved Product instance**
            stockQuantity = request.stockQuantity,
            price = request.price,
            imageUrl = imageUrl,
            color = color,
            size = size
        )
        return productItemRepository.save(productItem).toStoreDtoRes()
    }

    override fun update(productId: UUID, productItemId: UUID, request: ProductItemUpdateDtoReq): ProductItemDtoRes {
        val fetchedProductItem = productItemRepository.findByIdAndDeletedFalse(productItemId)
            .getOrNull() ?: throw EntityNotFoundException("Variação não encontrada")

        if (fetchedProductItem.product.id!! != productId) {
            throw EntityNotFoundException("Variação não encontrada")
        }

        // if user updated image
        request.image?.let {
            fetchedProductItem.imageUrl = imageService.uploadImage(request.image)
        }



        val updated = fetchedProductItem.apply {
            price = request.price
            stockQuantity = request.stockQuantity
        }

        return productItemRepository.save(updated).toDtoRes()
    }

    override fun delete(productId: UUID, productItemId: UUID, storeEmail: String) {
        val product = productRepository.findByIdAndDeletedFalse(productId)
            .getOrNull() ?: throw EntityNotFoundException("Produto não encontrado")

        if (!product.store.active || product.store.user.email != storeEmail) {
            throw EntityNotFoundException("Produto não encontrado")
        }

        val existing = productItemRepository.findByIdAndProductId(productItemId, productId)
            ?: throw EntityNotFoundException("Variação não encontrada")

        // delete
        existing.deleted = true

        productItemRepository.save(existing)
    }


}