package org.example.loja_das_coisas_api.models

import jakarta.persistence.*
import org.example.loja_das_coisas_api.common.BaseEntity

@Entity
@Table(name = "genders")
data class Gender(
    var name: String,
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    var deleted: Boolean = false,

    @ManyToMany(mappedBy = "genders")
    val categories: MutableList<Category> = mutableListOf(),
): BaseEntity()

