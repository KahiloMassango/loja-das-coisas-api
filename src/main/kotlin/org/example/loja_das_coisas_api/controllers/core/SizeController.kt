package org.example.loja_das_coisas_api.controllers.core

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.example.loja_das_coisas_api.common.APIResponse
import org.example.loja_das_coisas_api.common.Response
import org.example.loja_das_coisas_api.dtos.requests.SizeDtoReq
import org.example.loja_das_coisas_api.dtos.requests.SizeRequest
import org.example.loja_das_coisas_api.dtos.responses.SizeDtoRes
import org.example.loja_das_coisas_api.services.interfaces.SizeService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("v1/sizes")
@Tag(name = "Size", description = "Operations related to size management")
class SizeController(
    private val sizeService: SizeService
) {

    @Operation(summary = "Add sizes for the given category")
    @PostMapping
    fun createSizes(@RequestBody request: SizeRequest): ResponseEntity<Response<List<SizeDtoRes>>> {
        return ResponseEntity(
            APIResponse.success(sizeService.saveAll(request.categoryId,request.sizes)),
            HttpStatus.CREATED)
    }

    @Operation(summary = "Return all sizes of a category")
    @GetMapping
    fun getAllSize(): ResponseEntity<Response<List<SizeDtoRes>>> {
        return ResponseEntity(
            APIResponse.success(sizeService.getAllSizes()), HttpStatus.OK)
    }

    @Operation(summary = "Update size")
    @PutMapping("/{id}")
    fun updateSize(@PathVariable id: UUID, @RequestBody request: @Valid SizeDtoReq): ResponseEntity<Response<SizeDtoRes>> {
        return ResponseEntity(
            APIResponse.success(sizeService.update(id, request)),
            HttpStatus.OK)
    }

}