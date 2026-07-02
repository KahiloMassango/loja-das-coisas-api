package org.example.loja_das_coisas_api.product.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.example.loja_das_coisas_api.product.dto.request.ProductDtoReq
import org.example.loja_das_coisas_api.product.dto.request.ProductFilterRequest
import org.example.loja_das_coisas_api.product.dto.request.ProductUpdateDtoReq
import org.example.loja_das_coisas_api.product.dto.response.ProductStoreDtoRes
import org.example.loja_das_coisas_api.product.dto.response.ProductWithVariationStoreDtoRes
import org.example.loja_das_coisas_api.product.service.ProductService
import org.example.loja_das_coisas_api.shared.dto.APIResponse
import org.example.loja_das_coisas_api.shared.dto.Response
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("v1/stores/products")
@Tag(name = "Store Products", description = "Operations related to store product management")
class StoreProductController(
    private val productService: ProductService
) {

    @Operation(summary = "Create a new product for the authenticated store")
    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun createProduct(
        @ModelAttribute request: ProductDtoReq,
        authentication: Authentication
    ): ResponseEntity<Response<ProductStoreDtoRes>> {
        return ResponseEntity(
            APIResponse.success(productService.createProduct(authentication.name, request)),
            HttpStatus.CREATED
        )
    }

    @Operation(summary = "Get a specific product of the authenticated store")
    @GetMapping("/{productId}")
    fun getProductById(
        @PathVariable productId: UUID,
        authentication: Authentication
    ): ResponseEntity<Response<ProductWithVariationStoreDtoRes>> {
        return ResponseEntity.ok(
            APIResponse.success(productService.getProductByIdAndStoreId(authentication.name, productId))
        )
    }

    @Operation(summary = "Get all products of the authenticated store")
    @GetMapping
    fun getStoreProducts(authentication: Authentication): ResponseEntity<Response<List<ProductStoreDtoRes>>> {
        return ResponseEntity.ok(APIResponse.success(productService.getStoreProducts(authentication.name)))
    }

    @Operation(summary = "Filter the authenticated store's products by gender and/or category")
    @GetMapping("/filter")
    fun filterStoreProducts(
        @RequestParam gender: String?,
        @RequestParam category: String?,
        authentication: Authentication
    ): ResponseEntity<Response<List<ProductStoreDtoRes>>> {
        return ResponseEntity.ok(
            APIResponse.success(productService.filterForStore(authentication.name, gender, category))
        )
    }

    @Operation(summary = "Update a product of the authenticated store")
    @PutMapping("/{productId}", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun updateProduct(
        @PathVariable productId: UUID,
        @ModelAttribute request: ProductUpdateDtoReq,
        authentication: Authentication
    ): ResponseEntity<Response<ProductStoreDtoRes>> {
        return ResponseEntity.ok(
            APIResponse.success(productService.updateProduct(authentication.name, productId, request))
        )
    }

    @Operation(summary = "Delete a product of the authenticated store")
    @DeleteMapping("/{productId}")
    fun deleteProduct(
        @PathVariable productId: UUID,
        authentication: Authentication
    ): ResponseEntity<Unit> {
        productService.deleteProduct(authentication.name, productId)
        return ResponseEntity.noContent().build()
    }
}
