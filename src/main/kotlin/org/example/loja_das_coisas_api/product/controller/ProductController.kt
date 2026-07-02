package org.example.loja_das_coisas_api.product.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.example.loja_das_coisas_api.product.dto.response.ProductDtoRes
import org.example.loja_das_coisas_api.product.dto.response.ProductWithVariationDtoRes
import org.example.loja_das_coisas_api.product.service.ProductService
import org.example.loja_das_coisas_api.shared.dto.APIResponse
import org.example.loja_das_coisas_api.shared.dto.Response
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("v1/products")
@Tag(name = "Products", description = "Public product browsing endpoints")
class ProductController(
    private val productService: ProductService
) {

    @Operation(summary = "Get a product with all its variations by ID")
    @GetMapping("/{id}")
    fun getProductById(@PathVariable id: UUID): ResponseEntity<Response<ProductWithVariationDtoRes>> {
        return ResponseEntity.ok(APIResponse.success(productService.getProductById(id)))
    }

    @Operation(summary = "Search products by query string")
    @GetMapping("/search")
    fun search(@RequestParam query: String): ResponseEntity<Response<List<ProductDtoRes>>> {
        return ResponseEntity.ok(APIResponse.success(productService.search(query)))
    }

    @Operation(summary = "Filter products by gender and/or category")
    @GetMapping("/filter")
    fun filter(
        @RequestParam gender: String?,
        @RequestParam category: String?,
    ): ResponseEntity<Response<List<ProductDtoRes>>> {
        return ResponseEntity.ok(APIResponse.success(productService.filterProductsByGenderAndCategory(gender, category)))
    }

    @Operation(summary = "Get all active products for a given store")
    @GetMapping("/store/{storeId}")
    fun getProductsByStore(@PathVariable storeId: UUID): ResponseEntity<Response<List<ProductDtoRes>>> {
        return ResponseEntity.ok(APIResponse.success(productService.getProductsByStoreId(storeId)))
    }
}
