package org.example.loja_das_coisas_api.order.dto

data class CustomerOrdersDtoRes(
    val delivered: List<CustomerOrderDtoRes>,
    val pending: List<CustomerOrderDtoRes>,
    val canceled: List<CustomerOrderDtoRes>
)