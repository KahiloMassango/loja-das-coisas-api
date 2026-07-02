package org.example.loja_das_coisas_api.product.mapper

import org.example.loja_das_coisas_api.product.dto.response.*
import org.example.loja_das_coisas_api.product.model.*

fun Category.toDtoRes(): CategoryDtoRes = CategoryDtoRes(
    id = id!!,
    name = name,
    hasSizeVariation = hasSizeVariation,
    hasColorVariation = hasColorVariation
)

fun Color.toDtoRes() = ColorDtoRes(id = id!!, name = name)

fun Gender.toDtoRes(): GenderDtoRes = GenderDtoRes(
    id = id!!,
    name = name,
)

fun Size.toDtoRes() = SizeDtoRes(id = id!!, value = value, categoryId = category.id!!)

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
