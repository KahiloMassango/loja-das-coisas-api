package org.example.loja_das_coisas_api.order.dto

import java.math.BigDecimal
import java.util.UUID

data class CustomerOrderDetailDtoRes(
    val id: UUID,
    val storeName: String,
    val date: String,
    val subTotal: BigDecimal,
    val deliveryFee: BigDecimal,
    val total: BigDecimal,
    val deliveryAddressName: String,
    val paymentType: String,
    val status: String,
    val orderItems: List<OrderItemDtoRes>
)