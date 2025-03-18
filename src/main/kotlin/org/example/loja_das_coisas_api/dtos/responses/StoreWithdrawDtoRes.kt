package org.example.loja_das_coisas_api.dtos.responses


data class StoreFinanceStatus(
    val balance: Int,
    val pending: List<StoreWithdrawDtoRes>,
    val completed: List<StoreWithdrawDtoRes>
)

data class StoreWithdrawDtoRes(
    val amount: Int,
    val fee: Int,
    val feeAmount: Int,
    val total: Int,
    val requestDate: String
)
