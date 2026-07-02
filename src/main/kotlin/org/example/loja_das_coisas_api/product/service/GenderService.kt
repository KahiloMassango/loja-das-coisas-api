package org.example.loja_das_coisas_api.product.service

import jakarta.transaction.Transactional
import org.example.loja_das_coisas_api.product.dto.request.GenderDtoReq
import org.example.loja_das_coisas_api.product.dto.response.GenderDtoRes
import org.example.loja_das_coisas_api.product.model.Gender
import org.example.loja_das_coisas_api.product.repository.CategoryRepository
import org.example.loja_das_coisas_api.product.repository.GenderRepository
import org.example.loja_das_coisas_api.product.mapper.toDtoRes
import org.example.loja_das_coisas_api.exception.EntityNotFoundException
import org.springframework.stereotype.Service
import java.util.*

@Service
class GenderService(
    private val genderRepository: GenderRepository,
    private val categoryRepository: CategoryRepository
) {

    fun getAll(): List<GenderDtoRes> {
        return genderRepository.findAllByDeletedFalse().map { it.toDtoRes() }
    }

    fun findById(id: UUID): GenderDtoRes {
        validateIdExists(id)
        return genderRepository.getReferenceById(id).toDtoRes()
    }

    fun findByName(name: String): GenderDtoRes {
        val fetchedGender = genderRepository.findByNameEqualsAndDeletedFalse(name)
        if (fetchedGender.isPresent) {
            return fetchedGender.get().toDtoRes()
        }
        throw EntityNotFoundException("Gênero inválido")
    }

    fun delete(id: UUID) {
        validateIdExists(id)
        genderRepository.deleteById(id)
    }

    @Transactional
    fun create(request: GenderDtoReq): GenderDtoRes {
        val fetchedGender = genderRepository.findByNameEqualsAndDeletedFalse(request.name)
        require(!fetchedGender.isPresent) { "Gênero já existe!" }

        val categories = categoryRepository.findAllByNameInAndDeletedFalse(request.categories)
        if (categories.size != request.categories.size) {
            throw Exception("Há uma categoria inválida")
        }

        val gender = Gender(
            name = request.name,
            categories = categories.toMutableList()
        )
        return genderRepository.saveAndFlush(gender).toDtoRes()
    }

    fun update(id: UUID, request: GenderDtoReq): GenderDtoRes {
        validateIdExists(id)
        val existingGender = genderRepository.findById(id).get()
        existingGender.name = request.name
        return genderRepository.save(existingGender).toDtoRes()
    }

    private fun validateIdExists(id: UUID) {
        val fetchedGender = genderRepository.findById(id)
        require(fetchedGender.isPresent) { "Gênero não encontrado!" }
    }
}
