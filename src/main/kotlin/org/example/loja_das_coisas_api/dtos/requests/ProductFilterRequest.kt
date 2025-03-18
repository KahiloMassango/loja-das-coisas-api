package org.example.loja_das_coisas_api.dtos.requests

data class ProductFilterRequest(
    val gender: String? = null,
    val category: String? = null,
)
