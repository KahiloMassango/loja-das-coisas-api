package org.example.loja_das_coisas_api.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig : WebMvcConfigurer {
    // CORS is centrally and uniformly handled by SecurityConfig's CorsConfigurationSource bean
}