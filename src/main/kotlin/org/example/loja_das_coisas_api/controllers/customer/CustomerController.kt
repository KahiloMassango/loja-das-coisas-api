package org.example.loja_das_coisas_api.controllers.customer

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.example.loja_das_coisas_api.common.APIResponse
import org.example.loja_das_coisas_api.common.Response
import org.example.loja_das_coisas_api.dtos.requests.CustomerUpdateDtoReq
import org.example.loja_das_coisas_api.dtos.responses.CustomerDtoRes
import org.example.loja_das_coisas_api.services.interfaces.CustomerService
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
        @RequestBody
        request: CustomerUpdateDtoReq,
        authentication: Authentication
    ): ResponseEntity<Response<CustomerDtoRes>> {
        val customer = customerService.update(authentication.name, request)
        return ResponseEntity.ok(APIResponse.success(customer))
    }

    @Operation(summary = "Update authenticated customer password")
    @PutMapping("/update/password")
    fun updatePassword(
        @RequestBody
        request: String,
        authentication: Authentication
        ): ResponseEntity<Response<Unit>> {
        customerService.updatePassword(authentication.name, request)
        return ResponseEntity.ok(APIResponse.success(Unit))
    }

    @Operation(summary = "Get authenticated customer details")
    @GetMapping
    fun getCustomerDetails(
        authentication: Authentication
    ): ResponseEntity<Response<CustomerDtoRes>> {
        val customer = customerService.getDetails(authentication.name)
        return ResponseEntity.ok(APIResponse.success(customer))
    }


}