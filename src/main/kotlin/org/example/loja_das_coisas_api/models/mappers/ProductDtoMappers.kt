package org.example.loja_das_coisas_api.models.mappers

import org.example.loja_das_coisas_api.dtos.responses.*
import org.example.loja_das_coisas_api.models.Product

fun Product.toDtoRes(
    minPrice: Int
): ProductDtoRes = ProductDtoRes(
    id = id!!,
    name = name,
    imageUrl = imageUrl,
    description = description,
    storeId = store.id!!,
    storeName = store.storeName,
    minPrice = minPrice
)

fun Product.toStoreDtoRes() = ProductStoreDtoRes(
    id = id!!,
    name = name,
    imageUrl = imageUrl,
    description = description,
    storeId = store.id!!,
    storeName = store.storeName,
    isAvailable = available,
)


fun Product.toDtoResWithVariation(productItems: List<ProductItemDtoRes>) =
    ProductWithVariationDtoRes(
        id = id!!,
        name = name,
        imageUrl = imageUrl,
        description = description,
        productItems = productItems,
        storeId = store.id!!,
        storeName = store.storeName,
        category = category.toDtoRes(),
    )

fun Product.toStoreDtoResWithVariation(variations: List<ProductItemStoreDtoRes>) =
    ProductWithVariationStoreDtoRes(
        id = id!!,
        name = name,
        imageUrl = imageUrl,
        description = description,
        productItems = variations,
        storeId = store.id!!,
        storeName = store.storeName,
        isAvailable = available,
        category = category.toDtoRes(),
        gender = gender.toDtoRes()
    )

