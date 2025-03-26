package org.example.loja_das_coisas_api.utils.Config.Security

import org.example.loja_das_coisas_api.models.UserRole
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

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        // Define public and private routes

        http.authorizeHttpRequests {
            it.requestMatchers("/v1/auth/logout").hasAnyRole(UserRole.CUSTOMER.name, UserRole.STORE.name)
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
            it.requestMatchers("/swagger-ui/**","/swagger-resources/**", "/api-docs/**", "/webjars/**", "/swagger-ui-custom/**").permitAll()
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

        // Other configuration
        http.cors { it.disable() }
        http.sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
        http.csrf { it.disable() }
        http.headers { header ->
            header.frameOptions { it.disable() }
            header.xssProtection { it.disable() }
        }

        return http.build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        // allow localhost for dev purposes
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = listOf("http://localhost:3000", "http://localhost:8080", "https://silkworm-immortal-correctly.ngrok-free.app/qaaz")
        configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE")
        configuration.allowedHeaders = listOf("authorization", "content-type")
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }

}