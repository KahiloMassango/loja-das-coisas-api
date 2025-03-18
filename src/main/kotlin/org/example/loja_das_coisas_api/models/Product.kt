package org.example.loja_das_coisas_api.models

import jakarta.persistence.*
import org.example.loja_das_coisas_api.common.BaseEntity
import org.example.loja_das_coisas_api.models.store.Store


@Entity
@Table(name = "product")
data class Product(
    var name: String,
    var description: String,
    var available: Boolean,
    var imageUrl: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    var store: Store,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    var category: Category,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gender_id", nullable = false)
    var gender: Gender,

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    var deleted: Boolean = false,

    @OneToMany(mappedBy = "product", cascade = [CascadeType.PERSIST], fetch = FetchType.LAZY)
    val productItems: List<ProductItem> = emptyList()
) : BaseEntity()

