package org.example.loja_das_coisas_api.dtos.responses

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
