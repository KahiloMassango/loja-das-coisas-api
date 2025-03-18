package org.example.loja_das_coisas_api.controllers.core

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.example.loja_das_coisas_api.common.APIResponse
import org.example.loja_das_coisas_api.common.Response
import org.example.loja_das_coisas_api.dtos.requests.ColorDtoReq
import org.example.loja_das_coisas_api.dtos.responses.ColorDtoRes
import org.example.loja_das_coisas_api.services.interfaces.ColorService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
@RequestMapping("v1/colors")
@Tag(name = "Color", description = "Operations related to color management")
class ColorController(
    private val colorService: ColorService
) {

    @Operation(summary = "Add colors")
    @PostMapping
    fun createColors(@RequestBody request: List<ColorDtoReq>): ResponseEntity<Response<List<ColorDtoRes>>> {
        val createdColors = colorService.saveAll(request)
        return ResponseEntity(
            APIResponse.success(createdColors),
            HttpStatus.CREATED
        )
    }

    @Operation(summary = "Get all colors")
    @GetMapping
    fun getAllColors(): ResponseEntity<Response<List<ColorDtoRes>>> {
        return ResponseEntity(
            APIResponse.success(colorService.getAllColors()),
            HttpStatus.OK
        )
    }

    @Operation(summary = "Update color")
    @PutMapping("/{id}")
    fun updateColor(@PathVariable id: UUID, @RequestBody request: @Valid ColorDtoReq): ResponseEntity<Response<ColorDtoRes>> {
        return ResponseEntity(
            APIResponse.success(colorService.update(id, request)),
            HttpStatus.OK
        )
    }

    @Operation(summary = "Delete a color by ID")
    @DeleteMapping("/color/{id}")
    fun deleteColor(@PathVariable id: UUID): ResponseEntity<Void> {
        colorService.delete(id)
        return ResponseEntity.noContent().build()
    }

}