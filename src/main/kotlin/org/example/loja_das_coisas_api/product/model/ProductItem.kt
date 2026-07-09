package org.example.loja_das_coisas_api.product.model

import jakarta.persistence.*
import org.example.loja_das_coisas_api.shared.model.BaseEntity
import java.math.BigDecimal

@Entity
@Table
class ProductItem(
    @ManyToOne
    @JoinColumn
    var product: Product,

    var stockQuantity: Int,
    var price: BigDecimal,
    var imageUrl: String,
    @ManyToOne
    @JoinColumn
    var color: Color?,
    @ManyToOne
    @JoinColumn
    var size: Size?,
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    var deleted: Boolean = false
) : BaseEntity()
