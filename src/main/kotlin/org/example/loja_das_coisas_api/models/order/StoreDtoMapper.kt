package org.example.loja_das_coisas_api.models.order

import org.example.loja_das_coisas_api.dtos.responses.OrderItemStoreDtoRes
import org.example.loja_das_coisas_api.dtos.responses.order.StoreOrderDetailDtoRes
import org.example.loja_das_coisas_api.dtos.responses.order.StoreOrderDtoRes
import org.example.loja_das_coisas_api.models.mappers.toDateTime

fun Order.toStoreDtoRes(
    orderTotalItems: Int
) = StoreOrderDtoRes(
    id = id!!,
    total = total,
    date = createdAt!!.toDateTime().toLocalDate().toString(),
    customerName = customer.username,
    orderTotalItems = orderTotalItems,
    pending = status == OrderStatus.Processando,
)

fun Order.toStoreDetailDtoRes(
    orderItems: List<OrderItemStoreDtoRes>
) = StoreOrderDetailDtoRes(
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