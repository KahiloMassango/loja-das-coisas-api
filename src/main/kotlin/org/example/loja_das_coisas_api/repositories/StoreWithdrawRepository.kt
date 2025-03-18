package org.example.loja_das_coisas_api.repositories

import org.example.loja_das_coisas_api.models.store.StoreWithdraw
import org.example.loja_das_coisas_api.models.store.WithdrawStatus
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface StoreWithdrawRepository: JpaRepository<StoreWithdraw, UUID> {
    fun findAllByStoreIdAndStatus(storeId: UUID, status: WithdrawStatus): List<StoreWithdraw>
}