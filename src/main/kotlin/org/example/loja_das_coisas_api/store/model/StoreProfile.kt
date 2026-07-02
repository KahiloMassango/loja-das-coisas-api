package org.example.loja_das_coisas_api.store.model

import jakarta.persistence.*
import org.example.loja_das_coisas_api.auth.model.User
import org.example.loja_das_coisas_api.shared.model.BaseEntity
import org.example.loja_das_coisas_api.store.dto.StoreDtoRes
import org.example.loja_das_coisas_api.store.dto.StoreAdminDtoRes
import org.example.loja_das_coisas_api.store.dto.StoreDetailDtoRes
import org.example.loja_das_coisas_api.product.dto.response.ProductDtoRes
import org.example.loja_das_coisas_api.shared.util.toDateTime
import java.time.LocalTime

@Entity
@Table(name = "store")
 class StoreProfile(
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

fun StoreProfile.toStoreDtoRes() =
    StoreDtoRes(
        description = description,
        offersDelivery = offersDelivery,
        nif = nif,
        latitude = latitude,
        longitude = longitude,
        id = id!!,
        storeName = storeName,
        storeLogoUrl = storeLogoUrl,
    )

fun StoreProfile.toStoreAdminDtoRes() =
    StoreAdminDtoRes(
        id = id!!,
        name = storeName,
        description = description,
        offersDelivery = offersDelivery,
        nif = nif,
        memberSince = "${createdAt!!.toDateTime().month} ${createdAt!!.toDateTime().year}",
        latitude = latitude,
        longitude = longitude,
        email = user.email,
        phoneNumber = user.phoneNumber,
        logoUrl = storeLogoUrl,
        openingTime = openingTime.toString(),
        closingTime = closingTime.toString(),
        address = address,
        isActive = active
    )

fun StoreProfile.toStoreDetailDtoRes(
    totalProducts: Int,
    totalCompletedOrders: Int,
    products: List<ProductDtoRes>
) =
    StoreDetailDtoRes(
        name = storeName,
        description = description,
        offersDelivery = offersDelivery,
        nif = nif,
        memberSince = "${createdAt!!.toDateTime().month} ${createdAt!!.toDateTime().year}",
        latitude = latitude,
        longitude = longitude,
        totalProducts = totalProducts,
        totalCompletedOrders = totalCompletedOrders,
        products = products,
        logoUrl = storeLogoUrl,
        openingTime = openingTime.toString(),
        closingTime = closingTime.toString(),
        address = address
    )
