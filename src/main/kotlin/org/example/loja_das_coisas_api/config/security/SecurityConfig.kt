package org.example.loja_das_coisas_api.config.security

import org.example.loja_das_coisas_api.auth.model.UserRole
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @org.springframework.beans.factory.annotation.Value("\${cors.allowed-origins:*}")
    private val allowedOrigins: String = "*"

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        // Enable CORS first so preflight and headers are processed early
        http.cors { it.configurationSource(corsConfigurationSource()) }
        http.csrf { it.disable() }
        http.sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
        http.headers { header ->
            header.frameOptions { it.disable() }
            header.xssProtection { it.disable() }
        }

        // Define public and private routes
        http.authorizeHttpRequests {
            it.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            it.requestMatchers("/v1/auth/logout").authenticated()
            it.requestMatchers("/v1/auth/login").permitAll()
            it.requestMatchers("/v1/auth/create-account").permitAll()
            it.requestMatchers("/v1/auth/refresh-token").permitAll()
            it.requestMatchers("/v1/auth/register").hasRole(UserRole.CUSTOMER.name)
            it.requestMatchers(HttpMethod.GET, "/v1/stores/{id:[0-9a-fA-F-]{36}}").hasRole(UserRole.CUSTOMER.name)
            it.requestMatchers("/v1/stores/**").hasRole(UserRole.STORE.name)

            it.requestMatchers(HttpMethod.GET, "/v1/products/**").permitAll()
            it.requestMatchers(HttpMethod.GET, "/v1/images/**").permitAll()

            it.requestMatchers(HttpMethod.GET, "/v1/api/sync/**").permitAll()
            it.requestMatchers(HttpMethod.GET, "/v1/colors/**").hasAnyRole(UserRole.ADMIN.name)
            it.requestMatchers(HttpMethod.GET, "/v1/genders/**").hasAnyRole(UserRole.ADMIN.name)
            it.requestMatchers(HttpMethod.GET, "/v1/categories/**").hasAnyRole(UserRole.ADMIN.name)

            it.requestMatchers("/v1/customer/**").hasRole(UserRole.CUSTOMER.name)
            it.requestMatchers(
                "/actuator/**",
                "/swagger-ui/**",
                "/swagger-ui.html",
                "/swagger-resources/**",
                "/api-docs/**",
                "/v3/api-docs/**",
                "/webjars/**",
                "/swagger-ui-custom/**"
            ).permitAll()
            it.anyRequest().permitAll()
        }

        // Configure JWT
        http.oauth2ResourceServer {
            it.jwt { jwtConfigurer ->
                jwtConfigurer.jwtAuthenticationConverter { jwt ->
                    val role = jwt.getClaimAsString("role") ?: ""
                    val authorities = listOf(SimpleGrantedAuthority(role))
                    UsernamePasswordAuthenticationToken(jwt.subject, null, authorities)
                }
            }
        }

        return http.build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        val origins = allowedOrigins.split(",").map { it.trim() }.filter { it.isNotEmpty() }
        if (origins.isEmpty() || origins.contains("*")) {
            configuration.allowedOriginPatterns = listOf("*")
        } else {
            configuration.allowedOriginPatterns = origins
        }
        configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS", "HEAD")
        configuration.allowedHeaders = listOf("*")
        configuration.exposedHeaders = listOf("Authorization", "Content-Disposition", "*")
        configuration.allowCredentials = true
        configuration.maxAge = 3600L

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }

}