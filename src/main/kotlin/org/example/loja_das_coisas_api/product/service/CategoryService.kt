package org.example.loja_das_coisas_api.product.service

import jakarta.transaction.Transactional
import org.example.loja_das_coisas_api.product.dto.request.CategoryDtoReq
import org.example.loja_das_coisas_api.product.dto.request.CategoryUpdateDtoReq
import org.example.loja_das_coisas_api.product.dto.response.CategoryDtoRes
import org.example.loja_das_coisas_api.product.model.Category
import org.example.loja_das_coisas_api.product.repository.CategoryRepository
import org.example.loja_das_coisas_api.product.repository.GenderRepository
import org.example.loja_das_coisas_api.product.mapper.toDtoRes
import org.example.loja_das_coisas_api.exception.EntityNotFoundException
import org.springframework.stereotype.Service
import java.util.*
import kotlin.jvm.optionals.getOrNull

@Service
class CategoryService(
    private val categoryRepository: CategoryRepository,
    private val genderRepository: GenderRepository,
) {

    @Transactional
    fun createCategory(request: CategoryDtoReq): CategoryDtoRes {
        validateNameNotExists(request.name)

        val genders = request.genders.map { name ->
            genderRepository.findByNameEqualsAndDeletedFalse(name).getOrNull()
                ?: throw EntityNotFoundException("Gênero $name não existe")
        }

        val category = Category(
            name = request.name,
            hasSizeVariation = request.hasSizeVariations,
            hasColorVariation = request.hasColorVariations,
            genders = genders.toMutableList()
        )

        return categoryRepository.save(category).toDtoRes()
    }

    fun getAllCategories(): List<CategoryDtoRes> {
        return categoryRepository.findAllByDeletedFalse().map { it.toDtoRes() }
    }

    fun getAllCategoryEntities(): List<Category> {
        return categoryRepository.findAllByDeletedFalse()
    }

    fun findById(id: UUID): CategoryDtoRes {
        val fetchedCategory = categoryRepository.findByIdAndDeletedFalse(id)
        if (fetchedCategory.isPresent) {
            return fetchedCategory.get().toDtoRes()
        }
        throw EntityNotFoundException("Categoria não existe")
    }

    fun findByName(name: String): CategoryDtoRes {
        val fetchedCategory = categoryRepository.findByNameIsAndDeletedFalse(name)
        if (!fetchedCategory.isPresent) {
            throw EntityNotFoundException("Categoria Inválida")
        }
        return fetchedCategory.get().toDtoRes()
    }

    fun updateCategory(id: UUID, request: CategoryUpdateDtoReq): CategoryDtoRes {
        val fetchedCategory = categoryRepository.findByIdAndDeletedFalse(id)
        if (!fetchedCategory.isPresent) {
            throw EntityNotFoundException("Categoria não existe")
        }

        val updatedCategory = fetchedCategory.get().apply {
            name = request.name
        }
        return categoryRepository.save(updatedCategory).toDtoRes()
    }

    fun deleteCategory(id: UUID): Boolean {
        val category = categoryRepository.findById(id)
            .getOrNull() ?: throw EntityNotFoundException("Catégoria não encontrada")

        category.deleted = true
        categoryRepository.save(category)
        return true
    }

    private fun validateNameNotExists(categoryName: String) {
        val fetchedCategory = categoryRepository.findByNameIsAndDeletedFalse(categoryName)
        require(!fetchedCategory.isPresent) { "Já existe uma categoria com este nome" }
    }
}
