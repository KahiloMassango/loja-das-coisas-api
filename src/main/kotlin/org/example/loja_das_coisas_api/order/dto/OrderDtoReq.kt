package org.example.loja_das_coisas_api.order.dto

import java.math.BigDecimal
import java.util.UUID

data class OrderDtoReq(
    val subTotal: Int,
    val deliveryFeeAmount: BigDecimal,
    val total: Int,
    val deliveryAddressName: String,
    val latitude: Double,
    val longitude: Double,
    val paymentType: String,
    val orderItems: List<OrderItemDtoReq>
)

data class OrderItemDtoReq(
    val productItemId: UUID,
    val quantity: Int,
)