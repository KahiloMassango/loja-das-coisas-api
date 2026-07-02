package org.example.loja_das_coisas_api.order.dto

import java.util.*

// ── Order Item DTOs ──────────────────────────────────────────────

data class OrderItemDtoRes(
    val image: String,
    val color: String?,
    val size: String?,
    val quantity: Int,
    val price: Int,
)

data class OrderItemStoreDtoRes(
    val productName: String,
    val image: String,
    val color: String?,
    val size: String?,
    val quantity: Int,
    val price: Int,
)

data class OrderItemDtoReq(
    val productItemId: UUID,
    val quantity: Int,
)

// ── Customer Order DTOs ──────────────────────────────────────────

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

data class OrderDtoReq(
    val subTotal: Int,
    val deliveryFee: Int,
    val total: Int,
    val deliveryAddressName: String,
    val latitude: Double,
    val longitude: Double,
    val paymentType: String,
    val orderItems: List<OrderItemDtoReq>
)

// ── Store Order DTOs ──────────────────────────────────────────────

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
    val delivered: Boolean,
    val orderItems: List<OrderItemStoreDtoRes>
)
