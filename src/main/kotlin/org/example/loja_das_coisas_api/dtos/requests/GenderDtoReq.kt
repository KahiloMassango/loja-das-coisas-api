package org.example.loja_das_coisas_api.dtos.requests

data class GenderDtoReq(
    val name: String,
    val categories: List<String>
)
