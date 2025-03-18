package org.example.loja_das_coisas_api.controllers.store

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.example.loja_das_coisas_api.common.APIResponse
import org.example.loja_das_coisas_api.common.Response
import org.example.loja_das_coisas_api.dtos.requests.StoreDtoReq
import org.example.loja_das_coisas_api.dtos.responses.StoreDetailDtoRes
import org.example.loja_das_coisas_api.dtos.responses.StoreDtoRes
import org.example.loja_das_coisas_api.services.interfaces.StoreService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
@RequestMapping("v1/stores")
@Validated
@Tag(name = "Stores", description = "Operations related to stores management")
class StoreController(
    private val storeService: StoreService
) {

    @Operation(summary = "Register a new store", description = "Creates a new store with the provided details")
    @PostMapping
    fun createStore(@Valid @RequestBody request: StoreDtoReq): ResponseEntity<StoreDtoRes> {
        return ResponseEntity(storeService.createStore(request), HttpStatus.CREATED)
    }

    @Operation(summary = "Get authenticated store details")
    @GetMapping
    fun getAuthenticatedStore(authentication: Authentication): ResponseEntity<StoreDetailDtoRes> {
        return ResponseEntity.ok(storeService.storeDetails(authentication.name))
    }

    @Operation(summary = "Get store details by ID")
    @GetMapping("/{storeId}")
    fun getStoreById(@PathVariable storeId: UUID): ResponseEntity<Response<StoreDetailDtoRes>> {
        return ResponseEntity.ok(APIResponse.success(storeService.getStoreById(storeId)))
    }

    @Operation(summary = "Update authenticated store details")
    @PutMapping
    fun updateStore(
        @Valid @RequestBody request: StoreDtoReq,
        authentication: Authentication
    ): ResponseEntity<StoreDtoRes> {
        return ResponseEntity.ok(storeService.updateStore(authentication.name, request))
    }
}
