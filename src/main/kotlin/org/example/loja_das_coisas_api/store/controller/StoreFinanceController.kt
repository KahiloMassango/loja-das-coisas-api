package org.example.loja_das_coisas_api.store.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.example.loja_das_coisas_api.shared.dto.APIResponse
import org.example.loja_das_coisas_api.shared.dto.Response
import org.example.loja_das_coisas_api.store.dto.StoreFinanceStatus
import org.example.loja_das_coisas_api.store.service.StoreFinanceService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("v1/stores/finances")
@Tag(name = "Store Finances", description = "Manage store finances")
class StoreFinanceController(
    private val storeFinanceService: StoreFinanceService
) {

    @Operation(summary = "Get store finance status")
    @GetMapping
    fun getFinanceStatus(
        authentication: Authentication
    ): ResponseEntity<Response<StoreFinanceStatus>> {
        val status = storeFinanceService.getStoreFinanceStatus(authentication.name)
        return ResponseEntity(APIResponse.success(status), HttpStatus.OK)
    }

    @Operation(summary = "Store request withdraw")
    @GetMapping("request-withdraw")
    fun requestWithdraw(
        authentication: Authentication
    ): ResponseEntity<Unit> {
        storeFinanceService.requestWithdraw(authentication.name)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}
