package org.example.loja_das_coisas_api.services.impl

import jakarta.transaction.Transactional
import org.example.loja_das_coisas_api.dtos.requests.GenderDtoReq
import org.example.loja_das_coisas_api.dtos.responses.GenderDtoRes
import org.example.loja_das_coisas_api.exceptions.EntityNotFoundException
import org.example.loja_das_coisas_api.models.Gender
import org.example.loja_das_coisas_api.models.mappers.toDtoRes
import org.example.loja_das_coisas_api.repositories.CategoryRepository
import org.example.loja_das_coisas_api.repositories.GenderRepository
import org.example.loja_das_coisas_api.services.interfaces.GenderService
import org.springframework.stereotype.Service
import java.util.*

@Service
class GenderServiceImpl(
    private val genderRepository: GenderRepository,
    private val categoryRepository: CategoryRepository
) : GenderService {

    override fun getAll(): List<GenderDtoRes> {
        return genderRepository.findAllByDeletedFalse().map { it.toDtoRes() }
    }

    override fun findById(id: UUID): GenderDtoRes {
        validateIdExists(id)
        return genderRepository.getReferenceById(id).toDtoRes()
    }

    override fun findByName(name: String): GenderDtoRes {
        val fetchedGender = genderRepository.findByNameEqualsAndDeletedFalse(name)
        if (fetchedGender.isPresent) {
            return fetchedGender.get().toDtoRes()
        }
        throw EntityNotFoundException("Gênero inválido")
    }

    override fun delete(id: UUID) {
        validateIdExists(id)
        genderRepository.deleteById(id)
    }

    @Transactional
    override fun create(request: GenderDtoReq): GenderDtoRes {
        val fetchedGender = genderRepository.findByNameEqualsAndDeletedFalse(request.name)
        require(!fetchedGender.isPresent) { "Gênero já existe!" }


        val categories = categoryRepository.findAllByNameInAndDeletedFalse(request.categories)
        if(categories.size != request.categories.size) {
            throw Exception("Há uma categoria inválida")
        }

        val gender = Gender(
            name = request.name,
            categories = categories.toMutableList()
        )

        return genderRepository.saveAndFlush(gender).toDtoRes()

    }

    override fun update(id: UUID, request: GenderDtoReq): GenderDtoRes {
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