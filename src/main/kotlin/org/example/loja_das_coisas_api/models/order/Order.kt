package org.example.loja_das_coisas_api.models.order

import jakarta.persistence.*
import org.example.loja_das_coisas_api.common.BaseEntity
import org.example.loja_das_coisas_api.models.Customer
import org.example.loja_das_coisas_api.models.OrderItem
import org.example.loja_das_coisas_api.models.store.Store

@Entity
@Table(name = "orders")
data class Order(
    @ManyToOne(fetch = FetchType.LAZY)
    var customer: Customer,
    @ManyToOne(fetch = FetchType.LAZY)
    var store: Store,
    var productQty: Int,
    var subTotal: Int,
    var total: Int,
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: OrderStatus = OrderStatus.Processando,
    val paymentType: String,
    var deliveryFee: Int,
    var deliveryAddressName: String,
    var deliveryLatitude: Double,
    var deliveryLongitude: Double,
    @Column(nullable = false, columnDefinition = "BOOLEAN default false")
    var withdrawn: Boolean = false,
    @OneToMany(fetch = FetchType.EAGER)
    val orderItems: List<OrderItem> = emptyList()
) : BaseEntity()

enum class OrderStatus {
    Pendente, Processando, Entregue, Cancelado
}

