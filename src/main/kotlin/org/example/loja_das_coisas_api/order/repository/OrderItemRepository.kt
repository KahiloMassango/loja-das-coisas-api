package org.example.loja_das_coisas_api.order.repository

import org.example.loja_das_coisas_api.order.model.OrderItem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface OrderItemRepository : JpaRepository<OrderItem, UUID> {
    fun findAllByOrderId(orderId: UUID): List<OrderItem>
}
