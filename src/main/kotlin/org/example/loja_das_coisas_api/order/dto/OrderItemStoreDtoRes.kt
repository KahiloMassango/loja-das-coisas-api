package org.example.loja_das_coisas_api.order.dto

import java.math.BigDecimal

data class OrderItemStoreDtoRes(
    val productName: String,
    val image: String,
    val color: String?,
    val size: String?,
    val quantity: Int,
    val price: BigDecimal,
)
