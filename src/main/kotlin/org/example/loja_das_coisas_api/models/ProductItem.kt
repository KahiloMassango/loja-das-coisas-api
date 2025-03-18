package org.example.loja_das_coisas_api.models

import jakarta.persistence.*
import org.example.loja_das_coisas_api.common.BaseEntity

@Entity
@Table
data class ProductItem(
    @ManyToOne
    @JoinColumn(name = "product_id")
    var product: Product,

    var stockQuantity: Int,
    var price: Int,
    var imageUrl: String,
    @ManyToOne
    @JoinColumn(name = "color_id")
    var color: Color?,
    @ManyToOne
    @JoinColumn(name = "size_id")
    var size: Size?,
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    var deleted: Boolean = false

) : BaseEntity()
