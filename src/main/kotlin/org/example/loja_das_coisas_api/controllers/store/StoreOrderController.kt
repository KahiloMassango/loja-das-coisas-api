package org.example.loja_das_coisas_api.controllers.store

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.example.loja_das_coisas_api.common.APIResponse
import org.example.loja_das_coisas_api.common.Response
import org.example.loja_das_coisas_api.dtos.responses.order.StoreOrderDetailDtoRes
import org.example.loja_das_coisas_api.dtos.responses.order.StoreOrdersDtoRes
import org.example.loja_das_coisas_api.services.interfaces.OrderService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
@RequestMapping("v1/stores/orders")
@Validated
@Tag(name = "Store Orders", description = "Operations related to store orders management")
class StoreOrderController(
    private val orderService: OrderService
) {

    @Operation(summary = "Get all orders of the authenticated store")
    @GetMapping
    fun getAllStoreOrders(authentication: Authentication): ResponseEntity<Response<StoreOrdersDtoRes>> {
        val storeEmail = authentication.name
        return ResponseEntity.ok(APIResponse.success(orderService.getAllStoreOrders(storeEmail)))
    }

    @Operation(summary = "Get an order by ID")
    @GetMapping("/{id}")
    fun getOrderById(authentication: Authentication, @PathVariable id: UUID): ResponseEntity<Response<StoreOrderDetailDtoRes>> {
        val storeEmail = authentication.name
        return ResponseEntity.ok(APIResponse.success(orderService.getStoreOrderById(id, storeEmail)))
    }

    @Operation(summary = "Confirm order as delivered")
    @PatchMapping("/{id}/delivered")
    fun updateOrderDeliveredStatus(authentication: Authentication, @PathVariable id: UUID): ResponseEntity<Unit> {
        val storeEmail = authentication.name
        orderService.confirmOrderDelivered(id, storeEmail)
        return ResponseEntity.accepted().build()
    }
}
