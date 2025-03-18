package org.example.loja_das_coisas_api.models.store

import jakarta.persistence.*
import org.example.loja_das_coisas_api.common.BaseEntity
import org.example.loja_das_coisas_api.models.User
import java.time.LocalTime

@Entity
@Table
data class Store(
    @Column(unique = true)
    var storeName: String,
    @Column(nullable = false)
    var storeLogoUrl: String,
    @Column(nullable = false)
    var description: String,
    @Column(nullable = false)
    var nif: String,
    @Column(nullable = false)
    var latitude: Double,
    @Column(nullable = false)
    var longitude: Double,
    @Column(nullable = false)
    var address: String,
    @Column(nullable = false)
    var openingTime: LocalTime,
    @Column(nullable = false)
    var closingTime: LocalTime,
    @Column(nullable = false)
    var active: Boolean,
    var offersDelivery: Boolean,
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    var deleted: Boolean = false,
    var bankAccountName: String,
    var bankAccountIban: String,
    var fee: Int,
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    var user: User

) : BaseEntity()
