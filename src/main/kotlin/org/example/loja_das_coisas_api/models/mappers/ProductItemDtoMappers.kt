package org.example.loja_das_coisas_api.models.mappers

import org.example.loja_das_coisas_api.dtos.responses.ProductItemDtoRes
import org.example.loja_das_coisas_api.dtos.responses.ProductItemStoreDtoRes
import org.example.loja_das_coisas_api.models.ProductItem


fun ProductItem.toDtoRes(): ProductItemDtoRes = ProductItemDtoRes(
    id = id!!,
    stockQuantity = stockQuantity,
    price = price,
    imageUrl = imageUrl,
    color = color?.name,
    size = size?.value,
)

fun ProductItem.toStoreDtoRes(): ProductItemStoreDtoRes = ProductItemStoreDtoRes(
    id = id!!,
    stockQuantity = stockQuantity,
    price = price,
    imageUrl = imageUrl,
    color = color?.toDtoRes(),
    size = size?.toDtoRes()
)

