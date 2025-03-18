package org.example.loja_das_coisas_api.dtos.responses

import java.util.*

data class ProductItemDtoRes(
    val id: UUID,
    val stockQuantity: Int,
    val price: Int,
    val imageUrl: String?,
    val color: String?,
    val size: String?
)

data class ProductItemStoreDtoRes(
    val id: UUID,
    val stockQuantity: Int,
    val price: Int,
    val imageUrl: String?,
    val color: ColorDtoRes?,
    val size: SizeDtoRes?
)

