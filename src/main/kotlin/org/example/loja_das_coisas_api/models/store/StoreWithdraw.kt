package org.example.loja_das_coisas_api.models.store

import jakarta.persistence.*
import org.example.loja_das_coisas_api.common.BaseEntity
import org.example.loja_das_coisas_api.dtos.responses.StoreWithdrawDtoRes

@Entity
@Table(name = "store_withdraws")
data class StoreWithdraw(
    @ManyToOne(fetch = FetchType.LAZY)
    var store: Store,
    var amount: Int,
    var fee: Int,
    var feeAmount: Int,
    var total: Int,
    @Enumerated(EnumType.STRING)
    var status: WithdrawStatus = WithdrawStatus.Pending,
): BaseEntity()

fun StoreWithdraw.toDtoRes(): StoreWithdrawDtoRes =
    StoreWithdrawDtoRes(
        amount = amount,
        fee = fee,
        feeAmount = feeAmount,
        total = total,
        requestDate = createdAt.toString()
    )

enum class WithdrawStatus {
    Pending, Concluded
}
