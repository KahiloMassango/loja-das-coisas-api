package org.example.loja_das_coisas_api.order.model

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import org.example.loja_das_coisas_api.product.model.ProductItem
import org.example.loja_das_coisas_api.shared.model.BaseEntity

@Entity
@Table(name = "order_items")
data class OrderItem(
    @NotNull
    var productName: String,
    @ManyToOne(fetch = FetchType.LAZY)
    var order: Order,
    @ManyToOne(fetch = FetchType.LAZY)
    var productItem: ProductItem,
    var quantity: Int,
    var price: Int
): BaseEntity()
