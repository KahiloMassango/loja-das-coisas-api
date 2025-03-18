package org.example.loja_das_coisas_api.utils.Config

import io.minio.MinioClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MinioConfig {
    @Value("\${minio.access.key}")
    private val accessKey: String? = null

    @Value("\${minio.secret.key}")
    private val secretKey: String? = null

    @Value("\${minio.url}")
    private val minioUrl: String? = null

    @Bean
    fun minioClient(): MinioClient {
        return MinioClient.builder()
            .endpoint(minioUrl)
            .credentials(accessKey, secretKey)
            .build()
    }
}