package org.example.loja_das_coisas_api.models

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import org.example.loja_das_coisas_api.common.BaseEntity

@Entity
@Table
data class Color(
    var name: String,

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    var deleted: Boolean = false

) : BaseEntity()

