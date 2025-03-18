package org.example.loja_das_coisas_api.models

import jakarta.persistence.*
import org.example.loja_das_coisas_api.common.BaseEntity

@Entity
@Table(name = "users")
data class User(
    @Column(unique = true, nullable = false)
    var email: String,
    var password: String,

    @Enumerated(EnumType.STRING)
    var role: UserRole,

    @Column(unique = true, nullable = false)
    var phoneNumber: String,

    var deleted: Boolean = false,

    var refreshToken: String? = null,

): BaseEntity()

enum class UserRole {
    ADMIN, CUSTOMER, STORE
}
