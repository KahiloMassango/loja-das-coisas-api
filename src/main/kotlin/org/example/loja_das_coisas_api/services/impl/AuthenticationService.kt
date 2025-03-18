package org.example.loja_das_coisas_api.services.impl

import org.example.loja_das_coisas_api.controllers.AuthenticationRequest
import org.example.loja_das_coisas_api.controllers.AuthenticationResponse
import org.example.loja_das_coisas_api.dtos.requests.CustomerDtoReq
import org.example.loja_das_coisas_api.dtos.responses.CustomerDtoRes
import org.example.loja_das_coisas_api.exceptions.InvalidCredentialsException
import org.example.loja_das_coisas_api.exceptions.InvalidRefreshTokenException
import org.example.loja_das_coisas_api.repositories.UserRepository
import org.example.loja_das_coisas_api.services.interfaces.CustomerService
import org.example.loja_das_coisas_api.utils.Config.Security.TokenService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthenticationService(
    private val tokenService: TokenService,
    private val passwordEncoder: PasswordEncoder,
    private val userRepository: UserRepository,
    private val customerService: CustomerService,
) {
    fun authentication(authRequest: AuthenticationRequest):  AuthenticationResponse {
        val user = userRepository.findByEmailOrPhoneNumberAndDeletedFalse(authRequest.identifier)
            ?: throw InvalidCredentialsException()

        if (!passwordEncoder.matches(authRequest.password, user.password)) {
            throw InvalidCredentialsException()
        }

        val accessToken = tokenService.createAccessToken(user)
        val refreshToken = tokenService.createRefreshToken(user)

        user.apply {
            this.refreshToken = refreshToken
        }
        userRepository.save(user)

        return AuthenticationResponse(accessToken, refreshToken)
    }

    fun createAccount(request: CustomerDtoReq): CustomerDtoRes {
        return customerService.create(request)
    }

    fun refresh(refreshToken: String): AuthenticationResponse {
        val userEmail = tokenService.parseUserEmail(refreshToken)
        val user = userRepository.findByEmailOrPhoneNumberAndDeletedFalse(userEmail)!!

        if (user.refreshToken != refreshToken || tokenService.isTokenExpired(refreshToken)) {
            throw InvalidRefreshTokenException()
        }
        val newAccessToken = tokenService.createAccessToken(user)

        return AuthenticationResponse(newAccessToken, refreshToken)
    }

    fun logout(email: String) {
        val user = userRepository.findByEmailOrPhoneNumberAndDeletedFalse(email)
            ?: throw InvalidCredentialsException()

        user.apply {
            this.refreshToken = null
        }
        userRepository.save(user)
    }

}