package org.example.loja_das_coisas_api.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.example.loja_das_coisas_api.common.APIResponse
import org.example.loja_das_coisas_api.common.Response
import org.example.loja_das_coisas_api.dtos.responses.*
import org.example.loja_das_coisas_api.services.interfaces.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("v1/products")
@Tag(name = "Product", description = "Operations related to products")
class ProductController(
    private val productService: ProductService,
    private val categoryService: CategoryService,
    private val colorService: ColorService,
    private val sizeService: SizeService,
    private val genderService: GenderService
) {

    @Operation(summary = "Get products by gender and category")
    @GetMapping("/filter")
    fun getProductsByGenderAndCategory(
        @RequestParam("gender", required = false) gender: String?,
        @RequestParam("category", required = false) category: String?,
    ): ResponseEntity<Response<List<ProductDtoRes>>> {
        return ResponseEntity.ok(
            APIResponse.success(
                productService.filterProductsByGenderAndCategory(
                    gender,
                    category
                )
            )
        )
    }

    @Operation(summary = "Search products")
    @GetMapping("/search")
    fun search(@RequestParam("query") query: String, ): ResponseEntity<Response<List<ProductDtoRes>>> {
        return ResponseEntity.ok(APIResponse.success(productService.search(query)))
    }

    @Operation(summary = "Get a product by ID", description = "Returns a product with its variations")
    @GetMapping("/{id}")
    fun getProductById(@PathVariable id: UUID): ResponseEntity<Response<ProductWithVariationDtoRes>> {
        return ResponseEntity.ok(APIResponse.success(productService.getProductById(id)))
    }

    @Operation(summary = "Get products by store ID")
    @GetMapping("/store/{storeId}")
    fun getProductsByStore(
        @PathVariable storeId: UUID,
    ): ResponseEntity<Response<List<ProductDtoRes>>> {
        return ResponseEntity.ok(
            APIResponse.success(
                productService.getProductsByStoreId(storeId)
            )
        )
    }

    @Operation(summary = "Get all Category")
    @GetMapping("categories")
    fun getAllCategories(): ResponseEntity<Response<List<CategoryDtoRes>>> {
        return ResponseEntity.ok(APIResponse.success(categoryService.getAllCategories()))
    }

    @Operation(summary = "Get all colors")
    @GetMapping("colors")
    fun getAllColors(): ResponseEntity<List<ColorDtoRes>> {
        return ResponseEntity(colorService.getAllColors(), HttpStatus.OK)
    }

    @Operation(summary = "Get all genders")
    @GetMapping("genders")
    fun getAllGenders(): ResponseEntity<List<GenderDtoRes>> {
        return ResponseEntity(genderService.getAll(), HttpStatus.OK)
    }

    @Operation(summary = "Return all sizes of a category")
    @GetMapping("/category/{id}")
    fun getAllSize(@PathVariable id: UUID): ResponseEntity<List<SizeDtoRes>> {
        return ResponseEntity(sizeService.findByCategoryId(id), HttpStatus.OK)
    }

}