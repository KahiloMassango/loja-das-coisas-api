package org.example.loja_das_coisas_api.dtos.requests

data class CustomerDtoReq(
    val username: String,
    val email: String,
    val phoneNumber: String,
    val password: String
)

data class CustomerUpdateDtoReq(
    val username: String,
    val email: String,
    val phoneNumber: String,
)