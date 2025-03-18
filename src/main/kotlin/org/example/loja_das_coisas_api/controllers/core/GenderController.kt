package org.example.loja_das_coisas_api.controllers.core

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.example.loja_das_coisas_api.common.APIResponse
import org.example.loja_das_coisas_api.common.Response
import org.example.loja_das_coisas_api.dtos.requests.GenderDtoReq
import org.example.loja_das_coisas_api.dtos.responses.GenderDtoRes
import org.example.loja_das_coisas_api.services.interfaces.GenderService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("v1/genders")
@Tag(name = "Gender", description = "Operations related to gender management")
class GenderController(
    private val genderService: GenderService,
) {

    @Operation(summary = "Create a new gender")
    @PostMapping
    fun createGender(@RequestBody request: GenderDtoReq): ResponseEntity<Response<GenderDtoRes>> {
        val createdCategory = genderService.create(request)
        return ResponseEntity(APIResponse.success(createdCategory), HttpStatus.CREATED)
    }

    @Operation(summary = "Get all genders")
    @GetMapping
    fun getAllGenders(): ResponseEntity<Response<List<GenderDtoRes>>> {
        return ResponseEntity(APIResponse.success(genderService.getAll()), HttpStatus.OK)
    }


    @Operation(summary = "Update gender")
    @PutMapping("/{id}")
    fun updateGender(@PathVariable id: UUID, @RequestBody request: @Valid GenderDtoReq): ResponseEntity<Response<GenderDtoRes>> {
        return ResponseEntity(APIResponse.success(genderService.update(id, request)), HttpStatus.OK)
    }


    @Operation(summary = "Delete a gender by ID")
    @DeleteMapping("/{id}")
    fun deleteGender(@PathVariable id: UUID): ResponseEntity<Void> {
        genderService.delete(id)
        return ResponseEntity.noContent().build()
    }

}