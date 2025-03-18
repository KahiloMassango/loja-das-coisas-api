package org.example.loja_das_coisas_api.services.interfaces

import org.example.loja_das_coisas_api.dtos.requests.SizeDtoReq
import org.example.loja_das_coisas_api.dtos.responses.SizeDtoRes
import java.util.*

interface SizeService {
    fun findByName(name : String) : SizeDtoRes
    fun getAllSizes(): List<SizeDtoRes>
    fun findById(id : UUID) : SizeDtoRes
    fun findByIdAndCategoryId(id: UUID, categoryId: UUID): SizeDtoRes
    fun saveAll(categoryId: UUID, request : List<SizeDtoReq>) : List<SizeDtoRes>
    fun findByCategoryId(categoryId: UUID) : List<SizeDtoRes>
    fun update(id : UUID, request : SizeDtoReq): SizeDtoRes
    fun delete(id : UUID)
}
