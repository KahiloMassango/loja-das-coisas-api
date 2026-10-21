package org.example.loja_das_coisas_api.shared.infra

import io.swagger.v3.oas.annotations.tags.Tag
import org.example.loja_das_coisas_api.shared.service.ImageService
import org.springframework.http.MediaType
import org.springframework.http.MediaTypeFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("v1/images")
@Tag(name = "Image", description = "Operations related to Images")
class ImageController(
    private val service: ImageService
) {

    @GetMapping("/{imageName}")
    fun serveImage(
        @PathVariable("imageName") imageName: String
    ): ResponseEntity<ByteArray> {
        val resource = service.serveImage(imageName) ?: return ResponseEntity.notFound().build()
        val mediaType = MediaTypeFactory.getMediaType(imageName)
            .orElse(MediaType.IMAGE_JPEG)
        return ResponseEntity.ok()
            .contentType(mediaType)
            .body(resource)
    }
}

