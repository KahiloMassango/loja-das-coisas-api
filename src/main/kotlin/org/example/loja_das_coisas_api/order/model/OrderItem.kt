package org.example.loja_das_coisas_api.order.model

import jakarta.persistence.*
import org.example.loja_das_coisas_api.product.model.ProductItem
import org.example.loja_das_coisas_api.shared.model.BaseEntity
import java.math.BigDecimal

@Entity
@Table
class OrderItem(
    var productName: String,
    @ManyToOne(fetch = FetchType.LAZY)
    var order: Order,
    @ManyToOne(fetch = FetchType.LAZY)
    var productItem: ProductItem,
    var quantity: Int,
    var price: BigDecimal
): BaseEntity()
