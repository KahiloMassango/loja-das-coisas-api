package org.example.loja_das_coisas_api.services.impl

import jakarta.transaction.Transactional
import org.example.loja_das_coisas_api.dtos.requests.CategoryDtoReq
import org.example.loja_das_coisas_api.dtos.requests.CategoryUpdateDtoReq
import org.example.loja_das_coisas_api.dtos.responses.CategoryDtoRes
import org.example.loja_das_coisas_api.exceptions.EntityNotFoundException
import org.example.loja_das_coisas_api.models.Category
import org.example.loja_das_coisas_api.models.mappers.toDtoRes
import org.example.loja_das_coisas_api.repositories.CategoryRepository
import org.example.loja_das_coisas_api.repositories.GenderRepository
import org.example.loja_das_coisas_api.services.interfaces.CategoryService
import org.springframework.stereotype.Service
import java.util.*
import kotlin.jvm.optionals.getOrNull

@Service
class CategoryServiceImpl(
    private val categoryRepository: CategoryRepository,
    private val genderRepository: GenderRepository,
) : CategoryService {

    @Transactional
    override fun createCategory(request: CategoryDtoReq): CategoryDtoRes {
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

    override fun getAllCategories(): List<CategoryDtoRes> {
        return categoryRepository.findAllByDeletedFalse().map { it.toDtoRes() }
    }

    override fun findById(id: UUID): CategoryDtoRes {
        val fetchedCategory = categoryRepository.findByIdAndDeletedFalse(id)
        if (fetchedCategory.isPresent) {
            return fetchedCategory.get().toDtoRes()
        }
        throw EntityNotFoundException("Categoria não existe")
    }

    override fun findByName(name: String): CategoryDtoRes {
        val fetchedCategory = categoryRepository.findByNameIsAndDeletedFalse(name)
        if(!fetchedCategory.isPresent) {
            throw EntityNotFoundException("Categoria Inválida")
        }

        return fetchedCategory.get().toDtoRes()

    }

    override fun updateCategory(id: UUID, request: CategoryUpdateDtoReq): CategoryDtoRes {
        val fetchedCategory = categoryRepository.findByIdAndDeletedFalse(id)

        if (!fetchedCategory.isPresent) {
            throw EntityNotFoundException("Categoria não existe")

        }

        val updatedCategory = fetchedCategory.get().apply {
            name = request.name
        }
        return categoryRepository.save(updatedCategory).toDtoRes()

    }

    override fun deleteCategory(id: UUID): Boolean {
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
