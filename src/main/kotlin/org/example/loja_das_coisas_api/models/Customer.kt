package org.example.loja_das_coisas_api.models

import jakarta.persistence.*
import org.example.loja_das_coisas_api.common.BaseEntity

@Entity
@Table(name = "customer")
data class Customer(

    var username: String,

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    var deleted: Boolean = false,

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    var user: User

): BaseEntity()


