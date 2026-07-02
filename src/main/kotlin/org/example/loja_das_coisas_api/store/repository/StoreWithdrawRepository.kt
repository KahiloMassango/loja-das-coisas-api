package org.example.loja_das_coisas_api.store.repository

import org.example.loja_das_coisas_api.store.model.StoreWithdraw
import org.example.loja_das_coisas_api.store.model.WithdrawStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface StoreWithdrawRepository : JpaRepository<StoreWithdraw, UUID> {
    fun findAllByStoreIdAndStatus(storeId: UUID, status: WithdrawStatus): List<StoreWithdraw>
}
