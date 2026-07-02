package org.example.loja_das_coisas_api.order.mapper

import org.example.loja_das_coisas_api.order.dto.*
import org.example.loja_das_coisas_api.order.model.Order
import org.example.loja_das_coisas_api.order.model.OrderItem
import org.example.loja_das_coisas_api.order.model.OrderStatus
import org.example.loja_das_coisas_api.shared.util.toDateTime

// ── Customer mappers ─────────────────────────────────────────────

fun Order.toDtoRes(orderTotalItems: Int) = CustomerOrderDtoRes(
    id = id!!,
    total = total,
    date = createdAt.toString(),
    storeName = store.storeName,
    orderTotalItems = orderTotalItems
)

fun Order.toDetailDtoRes(orderItems: List<OrderItemDtoRes>) = CustomerOrderDetailDtoRes(
    id = id!!,
    storeName = store.storeName,
    date = createdAt.toString(),
    subTotal = subTotal,
    deliveryFee = deliveryFee,
    total = total,
    deliveryAddressName = deliveryAddressName,
    paymentType = paymentType,
    status = status.name,
    orderItems = orderItems
)

// ── Store mappers ─────────────────────────────────────────────────

fun Order.toStoreDtoRes(orderTotalItems: Int) = StoreOrderDtoRes(
    id = id!!,
    total = total,
    date = createdAt!!.toDateTime().toLocalDate().toString(),
    customerName = customer.username,
    orderTotalItems = orderTotalItems,
    pending = status == OrderStatus.Processando,
)

fun Order.toStoreDetailDtoRes(orderItems: List<OrderItemStoreDtoRes>) = StoreOrderDetailDtoRes(
    id = id!!,
    customerName = customer.username,
    customerPhoneNumber = customer.user.phoneNumber,
    date = createdAt!!.toDateTime().toLocalDate().toString(),
    subTotal = subTotal,
    deliveryFee = deliveryFee,
    total = total,
    deliveryAddressName = deliveryAddressName,
    paymentType = paymentType,
    delivered = status == OrderStatus.Entregue,
    orderItems = orderItems
)

// ── OrderItem mappers ─────────────────────────────────────────────

fun OrderItem.toDtoRes() = OrderItemDtoRes(
    quantity = quantity,
    price = price,
    image = productItem.imageUrl,
    color = productItem.color?.name,
    size = productItem.size?.value,
)

fun OrderItem.toStoreDtoRes() = OrderItemStoreDtoRes(
    productName = productName,
    quantity = quantity,
    price = price,
    image = productItem.imageUrl,
    color = productItem.color?.name,
    size = productItem.size?.value,
)
