package org.example.loja_das_coisas_api.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig {

    @Value("\${cors.allowed-origins:http://localhost:4200,http://localhost:3000,http://localhost:8080}")
    private val allowedOrigins: String = "http://localhost:4200,http://localhost:3000,http://localhost:8080"

    @Bean
    fun corsConfigurer(): WebMvcConfigurer {
        return object : WebMvcConfigurer {
            override fun addCorsMappings(registry: CorsRegistry) {
                val origins = allowedOrigins.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                val mapping = registry.addMapping("/**")
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                    .allowedHeaders("*")
                    .exposedHeaders("Authorization", "Content-Disposition")

                if (origins.contains("*")) {
                    mapping.allowedOriginPatterns("*")
                    mapping.allowCredentials(true)
                } else {
                    mapping.allowedOrigins(*origins.toTypedArray())
                    mapping.allowCredentials(true)
                }
            }
        }
    }
}