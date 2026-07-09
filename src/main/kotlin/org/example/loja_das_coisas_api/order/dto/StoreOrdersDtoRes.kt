package org.example.loja_das_coisas_api.order.dto

data class StoreOrdersDtoRes(
    val totalPendingOrders: Int,
    val totalDeliveredOrders: Int,
    val delivered: List<StoreOrderDtoRes>,
    val pending: List<StoreOrderDtoRes>,
)