package org.example.loja_das_coisas_api.services.impl

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

@Service
class ImageService {

    @Value("\${file.upload-dir}")
    private val uploadDir: String? = null

    fun uploadImage(image: MultipartFile): String {

        val extension = image.originalFilename?.substringAfterLast(".")
        val fileName = UUID.randomUUID().toString() + "." + extension

        // Set the file path where the image will be saved
        val filePath = Paths.get(uploadDir!!, fileName)

        // Ensure the directory exists
        val uploadPath = filePath.parent
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath)  // Create directories if not exist
        }
        try {
            val fileNameAndPath = Paths.get(uploadDir, fileName)
            Files.write(fileNameAndPath, image.bytes)
        } catch (e: Exception) {
            throw Exception("Failed to save the image file")
        }

        // Generate the URL to access the image
        val imageUrl = "https://silkworm-immortal-correctly.ngrok-free.app/v1/images/$fileName"

        return imageUrl
    }

    fun serveImage(imageName: String): ByteArray? {
        val imagePath = Paths.get(uploadDir!!).resolve(imageName)

        if (Files.exists(imagePath)) {
            val imageBytes = Files.readAllBytes(imagePath)
            return imageBytes
        } else {
            return null
        }
    }

}