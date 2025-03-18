package org.example.loja_das_coisas_api.dtos.requests

data class ProductFilterDtoReq(
    val maxPrice: Int?,
    val minPrice: Int?,
    val category: String?,
    val gender: String?,
)
