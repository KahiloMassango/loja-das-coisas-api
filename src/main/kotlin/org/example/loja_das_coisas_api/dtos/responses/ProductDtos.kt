package org.example.loja_das_coisas_api.dtos.responses

import java.util.*


data class ProductDtoRes(
    var id: UUID,
    var name: String,
    var imageUrl: String,
    var description: String,
    var storeId: UUID,
    var storeName: String,
    var minPrice: Int
)

data class ProductStoreDtoRes(
    var id: UUID,
    var name: String,
    var imageUrl: String,
    var description: String,
    var storeId: UUID,
    var storeName: String,
    var isAvailable: Boolean
)

data class ProductWithVariationDtoRes(
    var id: UUID,
    var name: String,
    var imageUrl: String,
    var description: String,
    var category: CategoryDtoRes,
    var productItems: List<ProductItemDtoRes>,
    var storeId: UUID,
    var storeName: String,
)

data class ProductWithVariationStoreDtoRes(
    var id: UUID,
    var name: String,
    var imageUrl: String,
    var description: String,
    var gender: GenderDtoRes,
    var category: CategoryDtoRes,
    var productItems: List<ProductItemStoreDtoRes>,
    var storeId: UUID,
    var storeName: String,
    var isAvailable: Boolean
)
