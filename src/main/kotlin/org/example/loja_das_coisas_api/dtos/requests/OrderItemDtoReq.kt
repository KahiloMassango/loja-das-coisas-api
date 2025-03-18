package org.example.loja_das_coisas_api.dtos.requests

import java.util.*

data class OrderItemDtoReq(
    val productItemId: UUID,
    val quantity: Int,
)