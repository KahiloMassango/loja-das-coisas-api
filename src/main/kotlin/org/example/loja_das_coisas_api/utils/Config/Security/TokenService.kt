package org.example.loja_das_coisas_api.utils.Config.Security

import org.example.loja_das_coisas_api.exceptions.InvalidRefreshTokenException
import org.example.loja_das_coisas_api.models.User
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.oauth2.jwt.*
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit

@Service
class TokenService(
    private val jwtDecoder: JwtDecoder,
    private val jwtEncoder: JwtEncoder,
    @Value("\${jwt.access.token.expiration}")
    private val accessExpiration: String,
    @Value("\${jwt.refresh.token.expiration}")
    private val refreshExpiration: String,
) {
    private val jwsHeader = JwsHeader.with { "HS256" }.build()
    fun createAccessToken(user: User): String {
        val claims = JwtClaimsSet.builder()
            .issuedAt(Instant.now())
            .expiresAt(Instant.now().plus(accessExpiration.toLong(), ChronoUnit.MILLIS))
            .subject(user.email)
            .claim("role", "ROLE_" + user.role)
            .build()
        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).tokenValue
    }

    fun createRefreshToken(user: User): String {
        val claims = JwtClaimsSet.builder()
            .issuedAt(Instant.now())
            .expiresAt(Instant.now().plus(refreshExpiration.toLong(), ChronoUnit.MILLIS))
            .subject(user.email)
            .build()
        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).tokenValue
    }

    fun isTokenExpired(token: String): Boolean {
        val jwt = jwtDecoder.decode(token)
        val expirationTime = jwt.expiresAt
        return Instant.now().isAfter(expirationTime)
    }

    fun parseUserEmail(token: String): String {
        return try {
            val jwt = jwtDecoder.decode(token)
            jwt.subject
        } catch (e: Exception) {
            throw InvalidRefreshTokenException()
        }
    }
}