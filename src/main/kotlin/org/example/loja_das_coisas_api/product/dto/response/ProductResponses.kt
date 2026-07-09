package org.example.loja_das_coisas_api.product.dto.response

import java.math.BigDecimal
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

data class ProductItemDtoRes(
    val id: UUID,
    val stockQuantity: Int,
    val price: BigDecimal,
    val imageUrl: String?,
    val color: String?,
    val size: String?
)

data class ProductItemStoreDtoRes(
    val id: UUID,
    val stockQuantity: Int,
    val price: BigDecimal,
    val imageUrl: String?,
    val color: ColorDtoRes?,
    val size: SizeDtoRes?
)
