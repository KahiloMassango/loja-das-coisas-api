package org.example.loja_das_coisas_api.controllers.customer

import io.swagger.v3.oas.annotations.tags.Tag
import org.example.loja_das_coisas_api.common.APIResponse
import org.example.loja_das_coisas_api.common.Response
import org.example.loja_das_coisas_api.dtos.requests.OrderDtoReq
import org.example.loja_das_coisas_api.dtos.responses.order.CustomerOrderDetailDtoRes
import org.example.loja_das_coisas_api.dtos.responses.order.CustomerOrderDtoRes
import org.example.loja_das_coisas_api.dtos.responses.order.CustomerOrdersDtoRes
import org.example.loja_das_coisas_api.services.interfaces.OrderService
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
    fun getAllCustomerOrders(
        authentication: Authentication
    ): ResponseEntity<Response<CustomerOrdersDtoRes>> {
        val orders = orderService.getAllCustomerOrders(authentication.name)
        return ResponseEntity.ok(APIResponse.success(orders))
    }

    @GetMapping("/{id}")
    fun getOrderById(
        @PathVariable id: UUID,
        authentication: Authentication
    ): ResponseEntity<Response<CustomerOrderDetailDtoRes>> {
        val order = orderService.getCustomerOrderById(id, authentication.name)
        return ResponseEntity.ok(APIResponse.success(order))
    }

    @PostMapping
    fun placeOrder(
        @RequestBody request: OrderDtoReq,
        authentication: Authentication
    ): ResponseEntity<Response<CustomerOrderDtoRes>> {
        val order = orderService.placeOrder(authentication.name, request)
        return ResponseEntity(APIResponse.success(order), HttpStatus.CREATED)
    }
}