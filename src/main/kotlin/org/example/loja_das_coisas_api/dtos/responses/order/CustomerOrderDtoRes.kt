package org.example.loja_das_coisas_api.dtos.responses.order

import org.example.loja_das_coisas_api.dtos.responses.OrderItemDtoRes
import java.util.*

data class CustomerOrderDtoRes(
    val id: UUID,
    val storeName: String,
    val date: String,
    val total: Int,
    val orderTotalItems: Int,
)

data class CustomerOrdersDtoRes(
    val delivered: List<CustomerOrderDtoRes>,
    val pending: List<CustomerOrderDtoRes>,
    val canceled: List<CustomerOrderDtoRes>
)

data class CustomerOrderDetailDtoRes(
    val id: UUID,
    val storeName: String,
    val date: String,
    val subTotal: Int,
    val deliveryFee: Int,
    val total: Int,
    val deliveryAddressName: String,
    val paymentType: String,
    val status: String,
    val orderItems: List<OrderItemDtoRes>
)
