package org.example.loja_das_coisas_api.customer.model

import jakarta.persistence.*
import org.example.loja_das_coisas_api.auth.model.User
import org.example.loja_das_coisas_api.shared.model.BaseEntity

@Entity
@Table(name = "customer")
data class CustomerProfile(

    var username: String,

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    var deleted: Boolean = false,

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    var user: User

): BaseEntity()
