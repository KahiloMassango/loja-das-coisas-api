package org.example.loja_das_coisas_api.shared.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

@Service
class ImageService {

    @Value("\${file.upload-dir:src/main/upload/images}")
    private val uploadDir: String = "src/main/upload/images"

    @Value("\${app.base-url:http://localhost:8080}")
    private val baseUrl: String = "http://localhost:8080"

    fun uploadImage(image: MultipartFile): String {
        val extension = image.originalFilename?.substringAfterLast(".") ?: "jpg"
        val fileName = UUID.randomUUID().toString() + "." + extension

        val uploadPath = Paths.get(uploadDir)
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath)
        }
        try {
            val destination = uploadPath.resolve(fileName)
            Files.write(destination, image.bytes)
        } catch (e: Exception) {
            throw Exception("Failed to save the image file: ${e.message}")
        }

        val cleanBaseUrl = baseUrl.trimEnd('/')
        return "$cleanBaseUrl/v1/images/$fileName"
    }

    fun serveImage(imageName: String): ByteArray? {
        val imagePath = Paths.get(uploadDir).resolve(imageName)
        return if (Files.exists(imagePath)) Files.readAllBytes(imagePath) else null
    }
}

