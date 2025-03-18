package org.example.loja_das_coisas_api.models.mappers

import org.example.loja_das_coisas_api.dtos.responses.OrderItemDtoRes
import org.example.loja_das_coisas_api.dtos.responses.OrderItemStoreDtoRes
import org.example.loja_das_coisas_api.models.OrderItem

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