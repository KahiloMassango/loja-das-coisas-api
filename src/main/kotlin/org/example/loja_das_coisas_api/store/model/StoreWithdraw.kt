package org.example.loja_das_coisas_api.store.model

import jakarta.persistence.*
import org.example.loja_das_coisas_api.shared.model.BaseEntity
import org.example.loja_das_coisas_api.store.dto.StoreWithdrawDtoRes
import java.math.BigDecimal

@Entity
@Table(name = "store_withdraws")
class StoreWithdraw(
    @ManyToOne(fetch = FetchType.LAZY)
    var store: StoreProfile,
    var amount: BigDecimal,
    var fee: Double,
    var feeAmount: BigDecimal,
    var total: BigDecimal,
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
