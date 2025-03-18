package org.example.loja_das_coisas_api.models.order

import org.example.loja_das_coisas_api.dtos.responses.OrderItemDtoRes
import org.example.loja_das_coisas_api.dtos.responses.order.CustomerOrderDetailDtoRes
import org.example.loja_das_coisas_api.dtos.responses.order.CustomerOrderDtoRes

fun Order.toDtoRes(
    orderTotalItems: Int
) = CustomerOrderDtoRes(
    id = id!!,
    total = total,
    date = createdAt.toString(),
    storeName = store.storeName,
    orderTotalItems = orderTotalItems
)

fun Order.toDetailDtoRes(
    orderItems: List<OrderItemDtoRes>
) = CustomerOrderDetailDtoRes(
    id = id!!,
    storeName = store.storeName,
    date = createdAt.toString(),
    subTotal = subTotal,
    deliveryFee = deliveryFee,
    total = total,
    deliveryAddressName = deliveryAddressName,
    paymentType = paymentType,
    deliveryMethod = deliveryMethod,
    status = status.name,
    orderItems = orderItems
)