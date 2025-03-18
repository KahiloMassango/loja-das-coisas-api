package org.example.loja_das_coisas_api.models

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import org.example.loja_das_coisas_api.common.BaseEntity
import org.example.loja_das_coisas_api.models.order.Order


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
