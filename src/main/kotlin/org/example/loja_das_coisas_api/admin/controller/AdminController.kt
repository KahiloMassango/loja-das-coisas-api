package org.example.loja_das_coisas_api.admin.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.example.loja_das_coisas_api.store.dto.StoreAdminDtoRes
import org.example.loja_das_coisas_api.store.service.StoreService
import org.example.loja_das_coisas_api.order.service.OrderService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("v1/admin")
@Tag(name = "Admin", description = "Operations related to ecommerce management")
class AdminController(
    private val storeService: StoreService,
    private val orderService: OrderService
) {

    @GetMapping("store/")
    @Operation(summary = "Get all stores")
    fun getAllStores(): ResponseEntity<List<StoreAdminDtoRes>> {
        val stores = storeService.getAllStores()
        return ResponseEntity(stores, HttpStatus.OK)
    }

    @PutMapping("store/{id}/status")
    @Operation(summary = "Updates store status")
    fun updateStoreStatus(@PathVariable id: UUID, @RequestBody status: Boolean): ResponseEntity<Unit> {
        storeService.updateStoreStatus(id, status)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("orders/{orderId}/{amount}")
    fun confirmOrderPayment(@PathVariable orderId: UUID, @PathVariable amount: Int): ResponseEntity<Unit> {
        orderService.confirmOrderPayment(orderId, amount)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("orders/{orderId}/cancel")
    fun cancelOrder(@PathVariable orderId: UUID): ResponseEntity<Unit> {
        orderService.cancelOrder(orderId)
        return ResponseEntity.noContent().build()
    }
}
