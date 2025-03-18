package org.example.loja_das_coisas_api.controllers.store

import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.tags.Tag
import org.example.loja_das_coisas_api.common.APIResponse
import org.example.loja_das_coisas_api.common.Response
import org.example.loja_das_coisas_api.dtos.requests.ProductItemDtoReq
import org.example.loja_das_coisas_api.dtos.requests.ProductItemUpdateDtoReq
import org.example.loja_das_coisas_api.dtos.responses.ProductItemDtoRes
import org.example.loja_das_coisas_api.dtos.responses.ProductItemStoreDtoRes
import org.example.loja_das_coisas_api.services.interfaces.ProductItemService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
@RequestMapping("v1/stores/products/{productId}/product-items")
@Tag(name = "Store Product Item", description = "Manage store product's product items")
class ProductItemController(
    private val productItemService: ProductItemService
) {

    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun addProductItem(
        @PathVariable productId: UUID,
        @RequestBody request: ProductItemDtoReq,
        authentication: Authentication
    ): ResponseEntity<Response<ProductItemStoreDtoRes>> {
        val productItem = productItemService.create(productId, request)
        return ResponseEntity(APIResponse.success(productItem), HttpStatus.CREATED)
    }

    @PutMapping("/{variationId}")
    fun updateProductItem(
        @PathVariable productId: UUID,
        @PathVariable variationId: UUID,
        @RequestBody request: ProductItemUpdateDtoReq,
        authentication: Authentication
    ): ResponseEntity<Response<ProductItemDtoRes>> {
        val variation = productItemService.update(productId, variationId, request)
        return ResponseEntity.ok(APIResponse.success(variation))
    }

    @DeleteMapping("/{variationId}")
    fun deleteProductItem(
        @PathVariable productId: UUID,
        @PathVariable variationId: UUID,
        authentication: Authentication
    ): ResponseEntity<Response<Unit>> {
        productItemService.delete(productId, variationId, authentication.name)
        return ResponseEntity.ok(APIResponse.success(Unit))
    }
}
