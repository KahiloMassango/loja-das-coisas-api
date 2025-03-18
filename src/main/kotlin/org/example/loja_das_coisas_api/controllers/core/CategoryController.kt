package org.example.loja_das_coisas_api.controllers.core

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.example.loja_das_coisas_api.common.APIResponse
import org.example.loja_das_coisas_api.common.Response
import org.example.loja_das_coisas_api.dtos.requests.CategoryDtoReq
import org.example.loja_das_coisas_api.dtos.requests.CategoryUpdateDtoReq
import org.example.loja_das_coisas_api.dtos.responses.CategoryDtoRes
import org.example.loja_das_coisas_api.services.interfaces.CategoryService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("v1/categories")
@Tag(name = "Category", description = "Operations related to category management")
class CategoryController(
    private val categoryService: CategoryService,
) {
    @Operation(summary = "Get all Category")
    @GetMapping
    fun getAllCategories(): ResponseEntity<Response<List<CategoryDtoRes>>> {
        return ResponseEntity.ok(APIResponse.success(categoryService.getAllCategories()))
    }

    @Operation(summary = "Create a new Category")
    @PostMapping
    fun createCategory(@RequestBody request: CategoryDtoReq): ResponseEntity<CategoryDtoRes?> {
        val createdCategory = categoryService.createCategory(request)
        return ResponseEntity(createdCategory, HttpStatus.CREATED)
    }

    @Operation(summary = "Get a category by ID")
    @GetMapping("/{id}")
    fun getCategoryById(@PathVariable id: UUID): ResponseEntity<CategoryDtoRes> {
        return ResponseEntity(categoryService.findById(id), HttpStatus.OK)
    }

    @Operation(summary = "Update a category",)
    @PutMapping("/{id}")
    fun updateCategory(
        @PathVariable id: UUID,
        @RequestBody request: @Valid CategoryUpdateDtoReq
    ): ResponseEntity<CategoryDtoRes> {
        return ResponseEntity(categoryService.updateCategory(id, request), HttpStatus.OK)

    }

    @Operation(summary = "Delete a category")
    @DeleteMapping("/{id}")
    fun deleteCategory(@PathVariable id: UUID): ResponseEntity<Void> {
        categoryService.deleteCategory(id)
        return ResponseEntity.noContent().build()

    }
}