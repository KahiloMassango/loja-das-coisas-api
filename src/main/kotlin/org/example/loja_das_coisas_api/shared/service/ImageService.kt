package org.example.loja_das_coisas_api.shared.service

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

        val filePath = Paths.get(uploadDir!!, fileName)
        val uploadPath = filePath.parent
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath)
        }
        try {
            val fileNameAndPath = Paths.get(uploadDir, fileName)
            Files.write(fileNameAndPath, image.bytes)
        } catch (e: Exception) {
            throw Exception("Failed to save the image file")
        }

        return "https://silkworm-immortal-correctly.ngrok-free.app/v1/images/$fileName"
    }

    fun serveImage(imageName: String): ByteArray? {
        val imagePath = Paths.get(uploadDir!!).resolve(imageName)
        return if (Files.exists(imagePath)) Files.readAllBytes(imagePath) else null
    }
}
