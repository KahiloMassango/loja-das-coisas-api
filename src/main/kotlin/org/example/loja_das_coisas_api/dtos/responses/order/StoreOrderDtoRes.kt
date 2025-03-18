package org.example.loja_das_coisas_api.dtos.responses.order

import org.example.loja_das_coisas_api.dtos.responses.OrderItemStoreDtoRes
import java.util.*

data class StoreOrderDtoRes(
    val id: UUID,
    val customerName: String,
    val date: String,
    val total: Int,
    val orderTotalItems: Int,
    val pending: Boolean
)

data class StoreOrdersDtoRes(
    val totalPendingOrders: Int,
    val totalDeliveredOrders: Int,
    val delivered: List<StoreOrderDtoRes>,
    val pending: List<StoreOrderDtoRes>,
)

data class StoreOrderDetailDtoRes(
    val id: UUID,
    val customerName: String,
    val customerPhoneNumber: String,
    val date: String,
    val subTotal: Int,
    val deliveryFee: Int,
    val total: Int,
    val deliveryAddressName: String,
    val paymentType: String,
    val deliveryMethod: String,
    val delivered: Boolean,
    val orderItems: List<OrderItemStoreDtoRes>
)
