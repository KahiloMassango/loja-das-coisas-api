package org.example.loja_das_coisas_api.services.impl

import org.example.loja_das_coisas_api.dtos.requests.SizeDtoReq
import org.example.loja_das_coisas_api.dtos.responses.SizeDtoRes
import org.example.loja_das_coisas_api.exceptions.AlreadyExistsException
import org.example.loja_das_coisas_api.exceptions.EntityNotFoundException
import org.example.loja_das_coisas_api.models.Size
import org.example.loja_das_coisas_api.models.mappers.toDtoRes
import org.example.loja_das_coisas_api.repositories.CategoryRepository
import org.example.loja_das_coisas_api.repositories.SizeRepository
import org.example.loja_das_coisas_api.services.interfaces.SizeService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import kotlin.jvm.optionals.getOrNull

@Service
class SizeServiceImpl(
    private val sizeRepository: SizeRepository,
    private val categoryRepository: CategoryRepository,
) : SizeService {

    override fun findByName(name: String): SizeDtoRes {
        val size = sizeRepository.findByValueAndDeletedFalse(name)
        if (size.isPresent) {
            return size.get().toDtoRes()
        }
        throw EntityNotFoundException("Tamanho nâo encontrado")
    }

    override fun findByCategoryId(categoryId: UUID): List<SizeDtoRes> {
        return sizeRepository.findByCategoryIdAndDeletedFalse(categoryId).map { it.toDtoRes() }
    }

    override fun getAllSizes(): List<SizeDtoRes> {
        return sizeRepository.findAllByDeletedFalse().map { it.toDtoRes() }
    }

    override fun findById(id: UUID): SizeDtoRes {
        val res = sizeRepository.findById(id)
        if (res.isPresent) {
            return res.get().toDtoRes()
        }
        throw EntityNotFoundException("Tamanho inválido")
    }

    override fun findByIdAndCategoryId(id: UUID, categoryId: UUID): SizeDtoRes {
        val size = sizeRepository.findByIdAndCategoryIdAndDeletedFalse(id, categoryId)
        if (size.isPresent) {
            return size.get().toDtoRes()
        }
        throw EntityNotFoundException("Tamanho nâo encontrado")
    }

    @Transactional
    override fun saveAll(categoryId: UUID, request: List<SizeDtoReq>): List<SizeDtoRes> {
        val category = categoryRepository.findByIdAndDeletedFalse(categoryId)
            .getOrNull() ?: throw EntityNotFoundException("Catégoria inválida")

        if(!category.hasSizeVariation) {
            throw Exception("Categoria não possui está variação")
        }

        // Validation
        request.forEach { size ->
            val existingSize = sizeRepository.findByValueAndCategoryIdAndDeletedFalse(size.value, categoryId)
            if(existingSize.isPresent) throw AlreadyExistsException("Tamanho ${existingSize.get().value} já existe!")
        }

        val size = request.map { size -> Size(size.value, category) }

        val sizes = sizeRepository.saveAll(size)
        return sizes.map { it.toDtoRes() }
    }

    override fun update(id: UUID, request: SizeDtoReq): SizeDtoRes {
        val existingSize = sizeRepository.findById(id)

        if (!existingSize.isPresent) {
            throw EntityNotFoundException("Tamanho não encontrado")
        }

        val updatedSize = existingSize.get().apply {
            value = request.value
        }
        return sizeRepository.save(updatedSize).toDtoRes()

    }

    override fun delete(id: UUID) {
        val existingSize = sizeRepository.findById(id)

        if (!existingSize.isPresent) {
            throw EntityNotFoundException("Tamanho não encontrado")
        }

        sizeRepository.deleteById(id)
    }

}