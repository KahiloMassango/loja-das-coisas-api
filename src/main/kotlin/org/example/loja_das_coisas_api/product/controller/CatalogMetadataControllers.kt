package org.example.loja_das_coisas_api.product.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.example.loja_das_coisas_api.product.dto.request.CategoryDtoReq
import org.example.loja_das_coisas_api.product.dto.request.CategoryUpdateDtoReq
import org.example.loja_das_coisas_api.product.dto.request.ColorDtoReq
import org.example.loja_das_coisas_api.product.dto.request.GenderDtoReq
import org.example.loja_das_coisas_api.product.dto.request.SizeDtoReq
import org.example.loja_das_coisas_api.product.dto.request.SizeRequest
import org.example.loja_das_coisas_api.product.dto.response.CategoryDtoRes
import org.example.loja_das_coisas_api.product.dto.response.ColorDtoRes
import org.example.loja_das_coisas_api.product.dto.response.GenderDtoRes
import org.example.loja_das_coisas_api.product.dto.response.SizeDtoRes
import org.example.loja_das_coisas_api.product.service.CategoryService
import org.example.loja_das_coisas_api.product.service.ColorService
import org.example.loja_das_coisas_api.product.service.GenderService
import org.example.loja_das_coisas_api.product.service.SizeService
import org.example.loja_das_coisas_api.shared.dto.APIResponse
import org.example.loja_das_coisas_api.shared.dto.Response
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("v1/categories")
@Tag(name = "Category", description = "Operations related to category management")
class CategoryController(private val categoryService: CategoryService) {

    @Operation(summary = "Get all categories")
    @GetMapping
    fun getAllCategories(): ResponseEntity<Response<List<CategoryDtoRes>>> =
        ResponseEntity.ok(APIResponse.success(categoryService.getAllCategories()))

    @Operation(summary = "Create a new category")
    @PostMapping
    fun createCategory(@RequestBody request: CategoryDtoReq): ResponseEntity<CategoryDtoRes?> {
        return ResponseEntity(categoryService.createCategory(request), HttpStatus.CREATED)
    }

    @Operation(summary = "Get a category by ID")
    @GetMapping("/{id}")
    fun getCategoryById(@PathVariable id: UUID): ResponseEntity<CategoryDtoRes> =
        ResponseEntity(categoryService.findById(id), HttpStatus.OK)

    @Operation(summary = "Update a category")
    @PutMapping("/{id}")
    fun updateCategory(
        @PathVariable id: UUID,
        @RequestBody request: @Valid CategoryUpdateDtoReq
    ): ResponseEntity<CategoryDtoRes> = ResponseEntity(categoryService.updateCategory(id, request), HttpStatus.OK)

    @Operation(summary = "Delete a category")
    @DeleteMapping("/{id}")
    fun deleteCategory(@PathVariable id: UUID): ResponseEntity<Void> {
        categoryService.deleteCategory(id)
        return ResponseEntity.noContent().build()
    }
}

@RestController
@RequestMapping("v1/colors")
@Tag(name = "Color", description = "Operations related to color management")
class ColorController(private val colorService: ColorService) {

    @Operation(summary = "Add colors")
    @PostMapping
    fun createColors(@RequestBody request: List<ColorDtoReq>): ResponseEntity<Response<List<ColorDtoRes>>> =
        ResponseEntity(APIResponse.success(colorService.saveAll(request)), HttpStatus.CREATED)

    @Operation(summary = "Get all colors")
    @GetMapping
    fun getAllColors(): ResponseEntity<Response<List<ColorDtoRes>>> =
        ResponseEntity(APIResponse.success(colorService.getAllColors()), HttpStatus.OK)

    @Operation(summary = "Update a color")
    @PutMapping("/{id}")
    fun updateColor(
        @PathVariable id: UUID,
        @RequestBody request: @Valid ColorDtoReq
    ): ResponseEntity<Response<ColorDtoRes>> =
        ResponseEntity(APIResponse.success(colorService.update(id, request)), HttpStatus.OK)

    @Operation(summary = "Delete a color by ID")
    @DeleteMapping("/color/{id}")
    fun deleteColor(@PathVariable id: UUID): ResponseEntity<Void> {
        colorService.delete(id)
        return ResponseEntity.noContent().build()
    }
}

@RestController
@RequestMapping("v1/genders")
@Tag(name = "Gender", description = "Operations related to gender management")
class GenderController(private val genderService: GenderService) {

    @Operation(summary = "Create a new gender")
    @PostMapping
    fun createGender(@RequestBody request: GenderDtoReq): ResponseEntity<Response<GenderDtoRes>> =
        ResponseEntity(APIResponse.success(genderService.create(request)), HttpStatus.CREATED)

    @Operation(summary = "Get all genders")
    @GetMapping
    fun getAllGenders(): ResponseEntity<Response<List<GenderDtoRes>>> =
        ResponseEntity(APIResponse.success(genderService.getAll()), HttpStatus.OK)

    @Operation(summary = "Update a gender")
    @PutMapping("/{id}")
    fun updateGender(
        @PathVariable id: UUID,
        @RequestBody request: @Valid GenderDtoReq
    ): ResponseEntity<Response<GenderDtoRes>> =
        ResponseEntity(APIResponse.success(genderService.update(id, request)), HttpStatus.OK)

    @Operation(summary = "Delete a gender by ID")
    @DeleteMapping("/{id}")
    fun deleteGender(@PathVariable id: UUID): ResponseEntity<Void> {
        genderService.delete(id)
        return ResponseEntity.noContent().build()
    }
}

@RestController
@RequestMapping("v1/sizes")
@Tag(name = "Size", description = "Operations related to size management")
class SizeController(private val sizeService: SizeService) {

    @Operation(summary = "Add sizes for a given category")
    @PostMapping
    fun createSizes(@RequestBody request: SizeRequest): ResponseEntity<Response<List<SizeDtoRes>>> =
        ResponseEntity(APIResponse.success(sizeService.saveAll(request.categoryId, request.sizes)), HttpStatus.CREATED)

    @Operation(summary = "Return all sizes")
    @GetMapping
    fun getAllSizes(): ResponseEntity<Response<List<SizeDtoRes>>> =
        ResponseEntity(APIResponse.success(sizeService.getAllSizes()), HttpStatus.OK)

    @Operation(summary = "Update a size")
    @PutMapping("/{id}")
    fun updateSize(
        @PathVariable id: UUID,
        @RequestBody request: @Valid SizeDtoReq
    ): ResponseEntity<Response<SizeDtoRes>> =
        ResponseEntity(APIResponse.success(sizeService.update(id, request)), HttpStatus.OK)
}
