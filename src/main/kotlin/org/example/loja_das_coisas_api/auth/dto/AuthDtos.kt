package org.example.loja_das_coisas_api.auth.dto

import org.example.loja_das_coisas_api.notification.model.DeviceType

data class LoginRequest(
    val identifier: String,
    val password: String,
    val deviceToken: String?,
    val deviceType: DeviceType?,
)

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String
)

data class RefreshTokenRequest(
    val refreshToken: String
)
