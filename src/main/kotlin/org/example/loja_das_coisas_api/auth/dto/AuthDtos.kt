package org.example.loja_das_coisas_api.auth.dto

data class LoginRequest(
    val identifier: String,
    val password: String,
)

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String
)

data class RefreshTokenRequest(
    val refreshToken: String
)
