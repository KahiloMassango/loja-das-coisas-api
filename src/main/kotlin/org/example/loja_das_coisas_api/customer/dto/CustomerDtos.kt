package org.example.loja_das_coisas_api.customer.dto

data class CustomerRegisterRequest(
    val username: String,
    val email: String,
    val phoneNumber: String,
    val password: String
)

data class CustomerUpdateRequest(
    val username: String,
    val email: String,
    val phoneNumber: String,
)

data class CustomerProfileResponse(
    val username: String,
    val email: String,
    val phoneNumber: String,
)
