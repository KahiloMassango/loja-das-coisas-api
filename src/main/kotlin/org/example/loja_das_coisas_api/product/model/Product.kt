package org.example.loja_das_coisas_api.product.model

import jakarta.persistence.*
import org.example.loja_das_coisas_api.shared.model.BaseEntity
import org.example.loja_das_coisas_api.store.model.StoreProfile

@Entity
@Table
class Product(
    var name: String,
    var imageUrl: String,
    var description: String,
    var available: Boolean,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    var store: StoreProfile,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    var category: Category,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gender_id", nullable = false)
    var gender: Gender,

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    var deleted: Boolean = false,

    @OneToMany(mappedBy = "product", cascade = [CascadeType.PERSIST], fetch = FetchType.LAZY)
    var productItems: MutableList<ProductItem> = mutableListOf()
) : BaseEntity()
