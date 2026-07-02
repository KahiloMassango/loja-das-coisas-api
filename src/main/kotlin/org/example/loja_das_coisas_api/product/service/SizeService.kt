package org.example.loja_das_coisas_api.product.service

import org.example.loja_das_coisas_api.product.dto.request.SizeDtoReq
import org.example.loja_das_coisas_api.product.dto.response.SizeDtoRes
import org.example.loja_das_coisas_api.product.model.Size
import org.example.loja_das_coisas_api.product.repository.CategoryRepository
import org.example.loja_das_coisas_api.product.repository.SizeRepository
import org.example.loja_das_coisas_api.product.mapper.toDtoRes
import org.example.loja_das_coisas_api.exception.AlreadyExistsException
import org.example.loja_das_coisas_api.exception.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import kotlin.jvm.optionals.getOrNull

@Service
class SizeService(
    private val sizeRepository: SizeRepository,
    private val categoryRepository: CategoryRepository,
) {

    fun findByName(name: String): SizeDtoRes {
        val size = sizeRepository.findByValueAndDeletedFalse(name)
        if (size.isPresent) {
            return size.get().toDtoRes()
        }
        throw EntityNotFoundException("Tamanho nâo encontrado")
    }

    fun findByCategoryId(categoryId: UUID): List<SizeDtoRes> {
        return sizeRepository.findByCategoryIdAndDeletedFalse(categoryId).map { it.toDtoRes() }
    }

    fun getAllSizes(): List<SizeDtoRes> {
        return sizeRepository.findAllByDeletedFalse().map { it.toDtoRes() }
    }

    fun findById(id: UUID): SizeDtoRes {
        val res = sizeRepository.findById(id)
        if (res.isPresent) {
            return res.get().toDtoRes()
        }
        throw EntityNotFoundException("Tamanho inválido")
    }

    fun findByIdAndCategoryId(id: UUID, categoryId: UUID): SizeDtoRes {
        val size = sizeRepository.findByIdAndCategoryIdAndDeletedFalse(id, categoryId)
        if (size.isPresent) {
            return size.get().toDtoRes()
        }
        throw EntityNotFoundException("Tamanho nâo encontrado")
    }

    @Transactional
    fun saveAll(categoryId: UUID, request: List<SizeDtoReq>): List<SizeDtoRes> {
        val category = categoryRepository.findByIdAndDeletedFalse(categoryId)
            .getOrNull() ?: throw EntityNotFoundException("Catégoria inválida")

        if (!category.hasSizeVariation) {
            throw Exception("Categoria não possui está variação")
        }

        request.forEach { size ->
            val existingSize = sizeRepository.findByValueAndCategoryIdAndDeletedFalse(size.value, categoryId)
            if (existingSize.isPresent) throw AlreadyExistsException("Tamanho ${existingSize.get().value} já existe!")
        }

        val size = request.map { size -> Size(size.value, category) }
        val sizes = sizeRepository.saveAll(size)
        return sizes.map { it.toDtoRes() }
    }

    fun update(id: UUID, request: SizeDtoReq): SizeDtoRes {
        val existingSize = sizeRepository.findById(id)
        if (!existingSize.isPresent) {
            throw EntityNotFoundException("Tamanho não encontrado")
        }

        val updatedSize = existingSize.get().apply {
            value = request.value
        }
        return sizeRepository.save(updatedSize).toDtoRes()
    }

    fun delete(id: UUID) {
        val existingSize = sizeRepository.findById(id)
        if (!existingSize.isPresent) {
            throw EntityNotFoundException("Tamanho não encontrado")
        }
        sizeRepository.deleteById(id)
    }
}
