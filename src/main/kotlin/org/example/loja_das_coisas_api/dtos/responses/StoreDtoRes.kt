package org.example.loja_das_coisas_api.dtos.responses

import java.util.*


data class StoreDtoRes(
    var id: UUID,
    var storeName: String,
    var storeLogoUrl: String,
    var description: String,
    var nif: String,
    var latitude: Double,
    var longitude: Double,
    var offersDelivery: Boolean,
)

data class StoreAdminDtoRes(
    val id: UUID,
    val name: String,
    val email: String,
    val phoneNumber: String,
    val logoUrl: String,
    val description: String,
    val offersDelivery: Boolean,
    val nif: String,
    val isActive: Boolean,
    val memberSince: String,
    val openingTime: String,
    val closingTime: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
)



data class StoreDetailDtoRes(
    val name: String,
    val logoUrl: String,
    val description: String,
    val offersDelivery: Boolean,
    val nif: String,
    val memberSince: String,
    val openingTime: String,
    val closingTime: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val totalProducts: Int,
    val totalCompletedOrders: Int,
    val products: List<ProductDtoRes>
)



