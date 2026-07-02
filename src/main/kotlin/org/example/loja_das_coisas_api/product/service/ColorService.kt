package org.example.loja_das_coisas_api.product.service

import org.example.loja_das_coisas_api.product.dto.request.ColorDtoReq
import org.example.loja_das_coisas_api.product.dto.response.ColorDtoRes
import org.example.loja_das_coisas_api.product.model.Color
import org.example.loja_das_coisas_api.product.repository.ColorRepository
import org.example.loja_das_coisas_api.product.mapper.toDtoRes
import org.example.loja_das_coisas_api.exception.EntityNotFoundException
import org.springframework.stereotype.Service
import java.util.*

@Service
class ColorService(
    private val colorRepository: ColorRepository
) {

    fun findByName(name: String): ColorDtoRes {
        val color = colorRepository.findByNameEqualsAndDeletedFalse(name)
        if (!color.isPresent) {
            throw EntityNotFoundException("Cor não encontrada")
        }
        return color.get().toDtoRes()
    }

    fun findById(id: UUID): ColorDtoRes {
        val color = colorRepository.findByIdAndDeletedFalse(id)
        if (!color.isPresent) {
            throw EntityNotFoundException("Cor não encontrada")
        }
        return color.get().toDtoRes()
    }

    fun saveAll(request: List<ColorDtoReq>): List<ColorDtoRes> {
        request.forEach {
            val existingColor = colorRepository.findByNameEqualsAndDeletedFalse(it.name)
            require(!existingColor.isPresent) { "Cor: ${existingColor.get().name} já existe" }
        }
        val colors = request.map { color -> Color(name = color.name) }
        return colorRepository.saveAllAndFlush(colors).map { it.toDtoRes() }
    }

    fun getAllColors(): List<ColorDtoRes> {
        return colorRepository.findAllByDeletedFalse().map { it.toDtoRes() }
    }

    fun add(request: ColorDtoReq): ColorDtoRes {
        val color = colorRepository.findByNameEqualsAndDeletedFalse(request.name)
        require(!color.isPresent) { "Cor já existe" }

        val created = colorRepository.save(Color(name = request.name))
        return created.toDtoRes()
    }

    fun update(id: UUID, request: ColorDtoReq): ColorDtoRes {
        val color = colorRepository.findByIdAndDeletedFalse(id)
        if (!color.isPresent) {
            throw EntityNotFoundException("Cor não encontrada")
        }

        val updated = color.get().apply {
            name = request.name
        }
        return colorRepository.save(updated).toDtoRes()
    }

    fun delete(id: UUID) {
        val color = colorRepository.findById(id)
        if (!color.isPresent) {
            throw EntityNotFoundException("Cor não encontrada")
        }
        colorRepository.save(color.get().copy(deleted = true))
    }
}
