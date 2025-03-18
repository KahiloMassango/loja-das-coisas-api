package org.example.loja_das_coisas_api.services.interfaces

import org.example.loja_das_coisas_api.dtos.requests.GenderDtoReq
import org.example.loja_das_coisas_api.dtos.responses.GenderDtoRes
import java.util.*


interface GenderService {
    fun getAll(): List<GenderDtoRes>
    fun findById(id: UUID): GenderDtoRes
    fun findByName(name: String): GenderDtoRes
    fun delete(id: UUID)
    fun create(request: GenderDtoReq): GenderDtoRes
    fun update(id: UUID, request: GenderDtoReq): GenderDtoRes
}