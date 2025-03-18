package org.example.loja_das_coisas_api.models

import jakarta.persistence.*
import org.example.loja_das_coisas_api.common.BaseEntity

@Entity
@Table
data class Size(
    var value: String,

    @ManyToOne
    @JoinColumn(name = "category_id")
    var category: Category,

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    var deleted: Boolean = false

) : BaseEntity()
