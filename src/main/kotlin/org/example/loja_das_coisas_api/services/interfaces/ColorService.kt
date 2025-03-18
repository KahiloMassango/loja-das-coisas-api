package org.example.loja_das_coisas_api.services.interfaces

import org.example.loja_das_coisas_api.dtos.requests.ColorDtoReq
import org.example.loja_das_coisas_api.dtos.responses.ColorDtoRes
import java.util.*

interface ColorService {
    fun findByName(name : String): ColorDtoRes
    fun findById(id: UUID): ColorDtoRes
    fun saveAll(request: List<ColorDtoReq>): List<ColorDtoRes>
    fun add(request : ColorDtoReq) : ColorDtoRes
    fun getAllColors() : List<ColorDtoRes>
    fun update(id : UUID, request : ColorDtoReq): ColorDtoRes
    fun delete(id : UUID)
}
