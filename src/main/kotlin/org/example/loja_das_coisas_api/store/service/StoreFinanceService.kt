package org.example.loja_das_coisas_api.store.service

import jakarta.transaction.Transactional
import org.example.loja_das_coisas_api.exception.EntityNotFoundException
import org.example.loja_das_coisas_api.exception.InsufficientBalanceException
import org.example.loja_das_coisas_api.order.repository.OrderRepository
import org.example.loja_das_coisas_api.store.dto.StoreFinanceStatus
import org.example.loja_das_coisas_api.store.dto.StoreWithdrawDtoRes
import org.example.loja_das_coisas_api.store.model.StoreWithdraw
import org.example.loja_das_coisas_api.store.model.WithdrawStatus
import org.example.loja_das_coisas_api.store.model.toDtoRes
import org.example.loja_das_coisas_api.store.repository.StoreRepository
import org.example.loja_das_coisas_api.store.repository.StoreWithdrawRepository
import org.springframework.stereotype.Service

@Service
class StoreFinanceService(
    private val storeWithdrawRepository: StoreWithdrawRepository,
    private val orderRepository: OrderRepository,
    private val storeRepository: StoreRepository
) {

    @Transactional
    fun requestWithdraw(storeEmail: String): StoreWithdrawDtoRes {
        val store = storeRepository.findByEmail(storeEmail) ?: throw EntityNotFoundException("Loja indisponível")

        val storeBalance = orderRepository.getStoreBalance(store.id!!)

        if (storeBalance <= 0) {
            throw InsufficientBalanceException()
        }

        val feeAmount = ((store.fee.toDouble() / 100) * storeBalance).toInt()

        val storeWithdraw = StoreWithdraw(
            store = store,
            amount = storeBalance,
            fee = store.fee,
            feeAmount = feeAmount,
            total = storeBalance - feeAmount,
        )

        orderRepository.updateOrdersWithdrawnTrue(store.id!!)

        return storeWithdrawRepository.save(storeWithdraw).toDtoRes()
    }

    fun getStoreFinanceStatus(storeEmail: String): StoreFinanceStatus {
        val store = storeRepository.findByEmail(storeEmail) ?: throw EntityNotFoundException("Loja indisponível")

        val storeBalance = orderRepository.getStoreBalance(store.id!!)

        val pendingWithdraw = storeWithdrawRepository.findAllByStoreIdAndStatus(store.id!!, WithdrawStatus.Pending)
            .map { it.toDtoRes() }
        val completedWithdraw = storeWithdrawRepository.findAllByStoreIdAndStatus(store.id!!, WithdrawStatus.Concluded)
            .map { it.toDtoRes() }

        return StoreFinanceStatus(
            balance = storeBalance,
            pending = pendingWithdraw,
            completed = completedWithdraw
        )
    }
}
