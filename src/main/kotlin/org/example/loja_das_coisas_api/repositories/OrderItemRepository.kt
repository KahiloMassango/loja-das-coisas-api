package org.example.loja_das_coisas_api.repositories

import org.example.loja_das_coisas_api.models.OrderItem
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface OrderItemRepository: JpaRepository<OrderItem, UUID> {

    fun findAllByOrderId(orderId: UUID): List<OrderItem>
}