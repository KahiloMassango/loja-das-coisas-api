package org.example.loja_das_coisas_api.product.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.example.loja_das_coisas_api.product.dto.request.ProductItemDtoReq
import org.example.loja_das_coisas_api.product.dto.request.ProductItemUpdateDtoReq
import org.example.loja_das_coisas_api.product.dto.response.ProductItemDtoRes
import org.example.loja_das_coisas_api.product.dto.response.ProductItemStoreDtoRes
import org.example.loja_das_coisas_api.product.service.ProductItemService
import org.example.loja_das_coisas_api.shared.dto.APIResponse
import org.example.loja_das_coisas_api.shared.dto.Response
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("v1/stores/products/{productId}/items")
@Tag(name = "Product Items", description = "Operations related to product item management")
class ProductItemController(
    private val productItemService: ProductItemService
) {

    @Operation(summary = "Create a new product item variation")
    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun createProductItem(
        @PathVariable productId: UUID,
        @ModelAttribute request: ProductItemDtoReq
    ): ResponseEntity<Response<ProductItemStoreDtoRes>> {
        return ResponseEntity(
            APIResponse.success(productItemService.create(productId, request)),
            HttpStatus.CREATED
        )
    }

    @Operation(summary = "Update a product item variation")
    @PutMapping("/{productItemId}", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun updateProductItem(
        @PathVariable productId: UUID,
        @PathVariable productItemId: UUID,
        @ModelAttribute request: ProductItemUpdateDtoReq
    ): ResponseEntity<Response<ProductItemDtoRes>> {
        return ResponseEntity.ok(APIResponse.success(productItemService.update(productId, productItemId, request)))
    }

    @Operation(summary = "Delete a product item variation")
    @DeleteMapping("/{productItemId}")
    fun deleteProductItem(
        @PathVariable productId: UUID,
        @PathVariable productItemId: UUID,
        authentication: Authentication
    ): ResponseEntity<Unit> {
        productItemService.delete(productId, productItemId, authentication.name)
        return ResponseEntity.noContent().build()
    }
}
