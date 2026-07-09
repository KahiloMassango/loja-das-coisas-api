package org.example.loja_das_coisas_api.order.dto

import java.math.BigDecimal
import java.util.UUID

data class CustomerOrderDtoRes(
    val id: UUID,
    val storeName: String,
    val date: String,
    val total: BigDecimal,
    val orderTotalItems: Int,
)