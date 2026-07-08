package org.example.loja_das_coisas_api.order.model

import jakarta.persistence.*
import org.example.loja_das_coisas_api.customer.model.CustomerProfile
import org.example.loja_das_coisas_api.shared.model.BaseEntity
import org.example.loja_das_coisas_api.store.model.StoreProfile

@Entity
@Table(name = "orders")
class Order(
    @ManyToOne(fetch = FetchType.LAZY)
    var customer: CustomerProfile,
    @ManyToOne(fetch = FetchType.LAZY)
    var store: StoreProfile,
    var productQty: Int,
    var subTotal: Int,
    var total: Int,
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: OrderStatus = OrderStatus.WaitingPayment,
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
    Processing, WaitingPayment, Delivered, Cancelled
}
