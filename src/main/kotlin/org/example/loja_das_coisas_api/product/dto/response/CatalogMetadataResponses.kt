package org.example.loja_das_coisas_api.product.dto.response

import java.util.*

data class CategoryDtoRes(
    val id: UUID,
    val name: String,
    val hasSizeVariation: Boolean,
    val hasColorVariation: Boolean
)

data class ColorDtoRes(
    val id: UUID,
    val name: String
)

data class GenderDtoRes(
    val id: UUID,
    val name: String,
)

data class SizeDtoRes(
    val id: UUID,
    val value: String,
    val categoryId: UUID,
)
