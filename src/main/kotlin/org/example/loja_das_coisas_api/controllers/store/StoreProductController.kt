package org.example.loja_das_coisas_api.controllers.store

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.example.loja_das_coisas_api.common.APIResponse
import org.example.loja_das_coisas_api.common.Response
import org.example.loja_das_coisas_api.dtos.requests.ProductDtoReq
import org.example.loja_das_coisas_api.dtos.requests.ProductUpdateDtoReq
import org.example.loja_das_coisas_api.dtos.responses.ProductStoreDtoRes
import org.example.loja_das_coisas_api.dtos.responses.ProductWithVariationStoreDtoRes
import org.example.loja_das_coisas_api.services.interfaces.ProductService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
@RequestMapping("v1/stores/products")
@Validated
@Tag(name = "Store Products", description = "Operations related to store products management")
class StoreProductController(
    private val productService: ProductService
) {

    @Operation(summary = "Get all products of the authenticated store")
    @GetMapping
    fun getProducts(authentication: Authentication): ResponseEntity<Response<List<ProductStoreDtoRes>>> {
        val storeEmail = authentication.name
        return ResponseEntity.ok(APIResponse.success(productService.getStoreProducts(storeEmail)))
    }

    @Operation(summary = "Get filtered products of the authenticated store")
    @GetMapping("/filter")
    fun getProductsByCategory(
        authentication: Authentication,
        @RequestParam gender: String?,
        @RequestParam category: String?
    ): ResponseEntity<Response<List<ProductStoreDtoRes>>> {
        val storeEmail = authentication.name
        return ResponseEntity.ok(APIResponse.success(productService.filterForStore(storeEmail, gender, category)))
    }

    @Operation(summary = "Create a new product")
    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun createProduct(
        authentication: Authentication,
        request: ProductDtoReq
    ): ResponseEntity<Response<ProductStoreDtoRes>> {
        val storeEmail = authentication.name
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(APIResponse.success(productService.createProduct(storeEmail, request)))
    }

    @Operation(summary = "Get a product by ID")
    @GetMapping("/{id}")
    fun getProductById(
        authentication: Authentication,
        @PathVariable id: UUID
    ): ResponseEntity<Response<ProductWithVariationStoreDtoRes>> {
        val storeEmail = authentication.name
        return ResponseEntity.ok(APIResponse.success(productService.getProductByIdAndStoreId(storeEmail, id)))
    }

    @Operation(summary = "Update a product")
    @PutMapping("/{id}", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun updateProduct(
        authentication: Authentication,
        @PathVariable id: UUID,
        request: ProductUpdateDtoReq
    ): ResponseEntity<Response<ProductStoreDtoRes>> {
        val storeEmail = authentication.name
        return ResponseEntity.ok(APIResponse.success(productService.updateProduct(storeEmail, id, request)))
    }

    @Operation(summary = "Delete a product")
    @DeleteMapping("/{id}")
    fun deleteProduct(authentication: Authentication, @PathVariable id: UUID): ResponseEntity<Void> {
        val storeEmail = authentication.name
        productService.deleteProduct(storeEmail, id)
        return ResponseEntity.noContent().build()
    }
}
