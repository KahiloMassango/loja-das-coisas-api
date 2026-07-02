package org.example.loja_das_coisas_api.customer.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.example.loja_das_coisas_api.customer.dto.CustomerProfileResponse
import org.example.loja_das_coisas_api.customer.dto.CustomerUpdateRequest
import org.example.loja_das_coisas_api.customer.service.CustomerService
import org.example.loja_das_coisas_api.shared.dto.APIResponse
import org.example.loja_das_coisas_api.shared.dto.Response
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("v1/customer")
@Tag(name = "Customer", description = "Operations related to customer")
class CustomerController(
    private val customerService: CustomerService,
) {

    @Operation(summary = "Update authenticated customer details")
    @PutMapping("/update")
    fun update(
        @RequestBody request: CustomerUpdateRequest,
        authentication: Authentication
    ): ResponseEntity<Response<CustomerProfileResponse>> {
        val customer = customerService.update(authentication.name, request)
        return ResponseEntity.ok(APIResponse.success(customer))
    }

    @Operation(summary = "Update authenticated customer password")
    @PutMapping("/update/password")
    fun updatePassword(
        @RequestBody request: String,
        authentication: Authentication
    ): ResponseEntity<Response<Unit>> {
        customerService.updatePassword(authentication.name, request)
        return ResponseEntity.ok(APIResponse.success(Unit))
    }

    @Operation(summary = "Get authenticated customer details")
    @GetMapping
    fun getCustomerDetails(
        authentication: Authentication
    ): ResponseEntity<Response<CustomerProfileResponse>> {
        val customer = customerService.getDetails(authentication.name)
        return ResponseEntity.ok(APIResponse.success(customer))
    }
}
