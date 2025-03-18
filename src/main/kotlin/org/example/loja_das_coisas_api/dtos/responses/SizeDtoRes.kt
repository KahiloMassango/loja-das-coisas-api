package org.example.loja_das_coisas_api.dtos.responses

import java.util.*

data class SizeDtoRes(
    val id: UUID,
    val value: String,
    val categoryId: UUID,
)
