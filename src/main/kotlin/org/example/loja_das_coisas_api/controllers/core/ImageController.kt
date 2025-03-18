package org.example.loja_das_coisas_api.controllers.core

import io.swagger.v3.oas.annotations.tags.Tag
import org.example.loja_das_coisas_api.services.impl.ImageService
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
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

    @Value("\${file.upload-dir}")
    val imageDirectory: String = ""
    @GetMapping("/{imageName}", produces = [MediaType.IMAGE_JPEG_VALUE])
    fun serveImage(
        @PathVariable ("imageName")
        imageName: String
    ): ResponseEntity<ByteArray> {
        val resource  = service.serveImage(imageName)
        return ResponseEntity.ok()
            .contentType(MediaType.IMAGE_JPEG)
            .body(resource)
    }

}