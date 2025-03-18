package org.example.loja_das_coisas_api.models

import jakarta.persistence.*
import org.example.loja_das_coisas_api.common.BaseEntity

@Entity
@Table
data class Category(
    var name: String,
    @Column(name = "has_size_variation", nullable = false)
    var hasSizeVariation: Boolean,

    @Column(name = "has_color_variation", nullable = false)
    var hasColorVariation: Boolean,

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    var deleted: Boolean = false,

    @ManyToMany(cascade = [(CascadeType.MERGE)])
    @JoinTable(
        name = "gender_category",
        joinColumns = [JoinColumn(name = "category_id")],
        inverseJoinColumns = [JoinColumn(name = "gender_id")]
    )
    val genders: MutableList<Gender> = mutableListOf(),
) : BaseEntity()



