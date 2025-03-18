package org.example.loja_das_coisas_api.services.impl

import org.example.loja_das_coisas_api.dtos.requests.ColorDtoReq
import org.example.loja_das_coisas_api.dtos.responses.ColorDtoRes
import org.example.loja_das_coisas_api.exceptions.EntityNotFoundException
import org.example.loja_das_coisas_api.models.Color
import org.example.loja_das_coisas_api.models.mappers.toDtoRes
import org.example.loja_das_coisas_api.repositories.ColorRepository
import org.example.loja_das_coisas_api.services.interfaces.ColorService
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service
import java.util.*

@Service
class ColorServiceImpl(
    private val colorRepository: ColorRepository,
    private val modelMapper: ModelMapper
): ColorService {

    override fun findByName(name: String): ColorDtoRes {
        val color = colorRepository.findByNameEqualsAndDeletedFalse(name)
        if (!color.isPresent) {
            throw EntityNotFoundException("Cor não encontrada")
        }
        return color.get().toDtoRes()
    }

    override fun findById(id: UUID): ColorDtoRes {
        val color = colorRepository.findByIdAndDeletedFalse(id)
        if (!color.isPresent) {
            throw EntityNotFoundException("Cor não encontrada")
        }
        return color.get().toDtoRes()
    }

    override fun saveAll(request: List<ColorDtoReq>): List<ColorDtoRes> {
        request.forEach {
            val existingColor = colorRepository.findByNameEqualsAndDeletedFalse(it.name)
            require(!existingColor.isPresent) { "Cor: ${existingColor.get().name} já existe" }
        }
        val colors = request.map { color -> Color(name = color.name) }
        return colorRepository.saveAllAndFlush(colors).map { it.toDtoRes() }
    }

    override fun getAllColors(): List<ColorDtoRes> {
        return colorRepository.findAllByDeletedFalse().map { it.toDtoRes() }
    }

    override fun add(request: ColorDtoReq): ColorDtoRes {
        val color = colorRepository.findByNameEqualsAndDeletedFalse(request.name)
        require(!color.isPresent) { "Cor já existe" }

        val created = colorRepository.save(modelMapper.map(request, Color::class.java))

        return created.toDtoRes()
    }

    override fun update(id : UUID, request: ColorDtoReq): ColorDtoRes {
        val color = colorRepository.findByIdAndDeletedFalse(id)

        if(!color.isPresent) {
            throw EntityNotFoundException("Cor não encontrada")
        }


        val updated = color.get().apply {
            name = request.name
        }

        return colorRepository.save(updated).toDtoRes()
    }

    override fun delete(id: UUID) {
        val color = colorRepository.findById(id)

        if(!color.isPresent) {
            throw EntityNotFoundException("Cor não encontrada")
        }
        colorRepository.save(color.get().copy(deleted = true))
    }
}