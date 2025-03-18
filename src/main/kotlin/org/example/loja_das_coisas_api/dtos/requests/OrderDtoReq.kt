package org.example.loja_das_coisas_api.dtos.requests

data class OrderDtoReq(
    val subTotal: Int,
    val deliveryFee: Int,
    val total: Int,
    val deliveryAddressName: String,
    val latitude: Double,
    val longitude: Double,
    val paymentType: String,
    val deliveryMethod: String,
    val orderItems: List<OrderItemDtoReq>
)
