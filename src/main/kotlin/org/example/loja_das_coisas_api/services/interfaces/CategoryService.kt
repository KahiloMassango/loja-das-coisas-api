package org.example.loja_das_coisas_api.services.interfaces

import org.example.loja_das_coisas_api.dtos.requests.CategoryDtoReq
import org.example.loja_das_coisas_api.dtos.requests.CategoryUpdateDtoReq
import org.example.loja_das_coisas_api.dtos.responses.CategoryDtoRes
import java.util.*

interface CategoryService {
    fun createCategory(request: CategoryDtoReq): CategoryDtoRes

    fun findByName(name: String): CategoryDtoRes

    fun getAllCategories(): List<CategoryDtoRes>

    fun findById(id: UUID): CategoryDtoRes

    fun updateCategory(id: UUID, request: CategoryUpdateDtoReq): CategoryDtoRes

    fun deleteCategory(id: UUID): Boolean
}
