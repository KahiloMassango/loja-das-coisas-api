package org.example.loja_das_coisas_api.controllers

import io.swagger.v3.oas.annotations.tags.Tag
import org.example.loja_das_coisas_api.common.APIResponse
import org.example.loja_das_coisas_api.common.Response
import org.example.loja_das_coisas_api.dtos.requests.CustomerDtoReq
import org.example.loja_das_coisas_api.dtos.responses.CustomerDtoRes
import org.example.loja_das_coisas_api.services.impl.AuthenticationService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("v1/auth")
@Tag(name = "Authentication", description = "Operations related to authentication")
class AuthController(
    private val authenticationService: AuthenticationService
) {
    @PostMapping("login")
    fun authenticate(
        @RequestBody request: AuthenticationRequest
    ): ResponseEntity<Response<AuthenticationResponse>> {
        return ResponseEntity(APIResponse.success(authenticationService.authentication(request)), HttpStatus.OK)
    }

    @PostMapping("create-account")
    fun createAccount(
        @RequestBody request: CustomerDtoReq
    ): ResponseEntity<Response<CustomerDtoRes>> {
        return ResponseEntity(APIResponse.success(authenticationService.createAccount(request)), HttpStatus.CREATED)
    }

    @PostMapping("refresh-token")
    fun refreshToken(
        @RequestBody request: RefreshTokenRequest
    ): ResponseEntity<Response<AuthenticationResponse>> {
        return ResponseEntity(APIResponse.success(authenticationService.refresh(request.refreshToken)), HttpStatus.OK)
    }

    @GetMapping("logout")
    fun logout(
        authentication: Authentication
    ): ResponseEntity<Response<Void>> {
        authenticationService.logout(authentication.name)
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

data class AuthenticationRequest(
    val identifier: String,
    val password: String,
)

data class AuthenticationResponse(
    val accessToken: String,
    val refreshToken: String
)

data class RefreshTokenRequest(
    val refreshToken: String
)