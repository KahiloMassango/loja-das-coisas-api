package org.example.loja_das_coisas_api.customer.controller

import io.swagger.v3.oas.annotations.tags.Tag
import org.example.loja_das_coisas_api.order.dto.CustomerOrderDetailDtoRes
import org.example.loja_das_coisas_api.order.dto.CustomerOrderDtoRes
import org.example.loja_das_coisas_api.order.dto.CustomerOrdersDtoRes
import org.example.loja_das_coisas_api.order.dto.OrderDtoReq
import org.example.loja_das_coisas_api.order.service.OrderService
import org.example.loja_das_coisas_api.shared.dto.APIResponse
import org.example.loja_das_coisas_api.shared.dto.Response
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("v1/customer/orders")
@Tag(name = "Customer Orders", description = "Operations related to customer orders management")
class CustomerOrderController(
    private val orderService: OrderService
) {
    @GetMapping
    fun getAllCustomerOrders(authentication: Authentication): ResponseEntity<Response<CustomerOrdersDtoRes>> {
        return ResponseEntity.ok(APIResponse.success(orderService.getAllCustomerOrders(authentication.name)))
    }

    @GetMapping("/{id}")
    fun getOrderById(
        @PathVariable id: UUID,
        authentication: Authentication
    ): ResponseEntity<Response<CustomerOrderDetailDtoRes>> {
        return ResponseEntity.ok(APIResponse.success(orderService.getCustomerOrderById(id, authentication.name)))
    }

    @PostMapping
    fun placeOrder(
        @RequestBody request: OrderDtoReq,
        authentication: Authentication
    ): ResponseEntity<Response<CustomerOrderDtoRes>> {
        return ResponseEntity(APIResponse.success(orderService.placeOrder(authentication.name, request)), HttpStatus.CREATED)
    }
}
