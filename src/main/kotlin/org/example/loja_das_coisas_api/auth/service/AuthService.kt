package org.example.loja_das_coisas_api.auth.service

import org.example.loja_das_coisas_api.auth.dto.LoginRequest
import org.example.loja_das_coisas_api.auth.dto.TokenResponse
import org.example.loja_das_coisas_api.auth.repository.UserRepository
import org.example.loja_das_coisas_api.config.security.TokenService
import org.example.loja_das_coisas_api.customer.dto.CustomerRegisterRequest
import org.example.loja_das_coisas_api.customer.dto.CustomerProfileResponse
import org.example.loja_das_coisas_api.customer.service.CustomerService
import org.example.loja_das_coisas_api.exception.DeviceTypeNotSetException
import org.example.loja_das_coisas_api.exception.InvalidCredentialsException
import org.example.loja_das_coisas_api.exception.InvalidRefreshTokenException
import org.example.loja_das_coisas_api.notification.service.DeviceService
import org.example.loja_das_coisas_api.notification.service.NotificationService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val tokenService: TokenService,
    private val passwordEncoder: PasswordEncoder,
    private val userRepository: UserRepository,
    private val customerService: CustomerService,
    private val deviceService: DeviceService,
    private val notificationService: NotificationService
) {
    fun authenticate(request: LoginRequest): TokenResponse {
        val user = userRepository.findByEmailOrPhoneNumberAndDeletedFalse(request.identifier)
            ?: throw InvalidCredentialsException()

        if (!passwordEncoder.matches(request.password, user.password)) {
            throw InvalidCredentialsException()
        }

        if (request.deviceToken != null) {
            if (request.deviceType == null) {
                throw DeviceTypeNotSetException()
            }

            deviceService.registerDevice(
                userId = user.id!!, deviceToken = request.deviceToken, deviceType = request.deviceType
            )
        }

        val accessToken = tokenService.createAccessToken(user)
        val refreshToken = tokenService.createRefreshToken(user)

        user.apply { this.refreshToken = refreshToken }
        userRepository.save(user)

        notificationService.sendPushToUser(user.id!!, "Login", "Welcome back")

        return TokenResponse(accessToken, refreshToken)
    }

    fun createAccount(request: CustomerRegisterRequest): CustomerProfileResponse {
        return customerService.create(request)
    }

    fun refresh(refreshToken: String): TokenResponse {
        val userEmail = tokenService.parseUserEmail(refreshToken)
        val user = userRepository.findByEmailOrPhoneNumberAndDeletedFalse(userEmail)!!

        if (user.refreshToken != refreshToken || tokenService.isTokenExpired(refreshToken)) {
            throw InvalidRefreshTokenException()
        }
        val newAccessToken = tokenService.createAccessToken(user)

        return TokenResponse(newAccessToken, refreshToken)
    }

    fun logout(email: String, refreshToken: String, deviceToken: String) {
        val user = userRepository.findByEmailOrPhoneNumberAndDeletedFalse(email)
            ?: throw InvalidCredentialsException()

        user.apply { this.refreshToken = null }
        deviceService.removeDevice(deviceToken)
        userRepository.saveAndFlush(user)
    }
}
