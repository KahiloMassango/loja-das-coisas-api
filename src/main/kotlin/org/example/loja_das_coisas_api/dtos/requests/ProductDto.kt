package org.example.loja_das_coisas_api.dtos.requests

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.web.multipart.MultipartFile
import java.util.*


data class ProductDtoReq(
    @get: NotBlank(message = "O nome do producto é obrigatório")
    val name: String,
    val isAvailable: Boolean,
    val description: String,
    val image: MultipartFile,
    @get: NotNull(message = "O producto deve ter uma categoria")
    val categoryId: UUID,
    val genderId: UUID,
)


data class ProductUpdateDtoReq(
    @get: NotBlank(message = "O nome do producto é obrigatório")
    val name: String,
    val isAvailable: Boolean,
    val description: String,
    val image: MultipartFile?,
)


