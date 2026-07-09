package org.example.loja_das_coisas_api.order.dto

import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

data class StoreOrderDetailDtoRes(
    val id: UUID,
    val customerName: String,
    val customerPhoneNumber: String,
    val date: LocalDateTime,
    val subTotal: BigDecimal,
    val deliveryFee: BigDecimal,
    val total: BigDecimal,
    val deliveryAddressName: String,
    val paymentType: String,
    val delivered: Boolean,
    val orderItems: List<OrderItemStoreDtoRes>
)
