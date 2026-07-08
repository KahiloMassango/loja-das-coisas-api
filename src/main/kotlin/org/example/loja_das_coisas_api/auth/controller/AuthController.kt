package org.example.loja_das_coisas_api.auth.controller

import io.swagger.v3.oas.annotations.tags.Tag
import org.example.loja_das_coisas_api.auth.dto.LoginRequest
import org.example.loja_das_coisas_api.auth.dto.RefreshTokenRequest
import org.example.loja_das_coisas_api.auth.dto.TokenResponse
import org.example.loja_das_coisas_api.auth.service.AuthService
import org.example.loja_das_coisas_api.customer.dto.CustomerProfileResponse
import org.example.loja_das_coisas_api.customer.dto.CustomerRegisterRequest
import org.example.loja_das_coisas_api.shared.dto.APIResponse
import org.example.loja_das_coisas_api.shared.dto.Response
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("v1/auth")
@Tag(name = "Authentication", description = "Operations related to authentication")
class AuthController(
    private val authService: AuthService
) {
    @PostMapping("login")
    fun authenticate(
        @RequestBody request: LoginRequest
    ): ResponseEntity<Response<TokenResponse>> {
        return ResponseEntity(APIResponse.success(authService.authenticate(request)), HttpStatus.OK)
    }

    @PostMapping("create-account")
    fun createAccount(
        @RequestBody request: CustomerRegisterRequest
    ): ResponseEntity<Response<CustomerProfileResponse>> {
        return ResponseEntity(APIResponse.success(authService.createAccount(request)), HttpStatus.CREATED)
    }

    @PostMapping("refresh-token")
    fun refreshToken(
        @RequestBody request: RefreshTokenRequest
    ): ResponseEntity<Response<TokenResponse>> {
        return ResponseEntity(APIResponse.success(authService.refresh(request.refreshToken)), HttpStatus.OK)
    }

    @PostMapping("logout")
    fun logout(
        authentication: Authentication,
        @RequestBody refreshToken: String,
        @RequestBody deviceToken: String,
    ): ResponseEntity<Response<Void>> {
        authService.logout(
            email = authentication.name,
            refreshToken = refreshToken,
            deviceToken = deviceToken
        )
        return ResponseEntity.status(HttpStatus.OK).body(null)
    }

    @GetMapping("get")
    fun get(
        authentication: Authentication
    ): String {
        println(authentication.toString())
        return authentication.name
    }
}
