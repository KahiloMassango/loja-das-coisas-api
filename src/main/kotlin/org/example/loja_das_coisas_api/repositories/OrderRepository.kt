package org.example.loja_das_coisas_api.repositories

import org.example.loja_das_coisas_api.models.order.Order
import org.example.loja_das_coisas_api.models.order.OrderStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface OrderRepository: JpaRepository<Order, UUID> {
    fun findAllByCustomerIdAndStatusIn(customerId: UUID, status: List<OrderStatus>): List<Order>
    fun findByIdAndCustomerId(id: UUID, customerId: UUID): Optional<Order>

    @Query("select count(*) from Order o where o.store.id = :storeId and o.status = 'Entregue'")
    fun findAllStoreCompletedOrders(storeId: UUID): Int

    @Query("select count(i) from Order o inner join OrderItem i on o.id = i.order.id where o.id = :orderId")
    fun getOrderItemsCountByOrderId(orderId: UUID): Int

    @Query("select count(o) from Order o where o.store.id = :storeId and o.status = :status")
    fun getStoreTotalOrdersByStatus(storeId: UUID, status: OrderStatus): Int

    fun findAllByStoreIdAndStatusIn(storeId: UUID, status: List<OrderStatus>): List<Order>
    fun findByIdAndStoreIdAndStatusLike(id: UUID, storeId: UUID, status: OrderStatus): Optional<Order>
    fun findByIdAndStoreIdAndStatusIn(id: UUID, storeId: UUID, status: List<OrderStatus>): Optional<Order>

    @Query("select coalesce(sum(o.total), 0) from Order o where o.store.id = :storeId and o.status = :status and o.withdrawn = false")
    fun getStoreBalance(storeId: UUID, status: OrderStatus = OrderStatus.Entregue): Int

    @Modifying
    @Query("UPDATE Order o SET o.withdrawn = true WHERE o.store.id = :storeId and o.status = :status and o.withdrawn = false")
    fun updateOrdersWithdrawnTrue(storeId: UUID, status: OrderStatus = OrderStatus.Entregue)

}