package org.example.loja_das_coisas_api.services.impl

import jakarta.transaction.Transactional
import org.example.loja_das_coisas_api.dtos.responses.StoreFinanceStatus
import org.example.loja_das_coisas_api.dtos.responses.StoreWithdrawDtoRes
import org.example.loja_das_coisas_api.exceptions.EntityNotFoundException
import org.example.loja_das_coisas_api.exceptions.InsufficientBalanceException
import org.example.loja_das_coisas_api.models.store.StoreWithdraw
import org.example.loja_das_coisas_api.models.store.WithdrawStatus
import org.example.loja_das_coisas_api.models.store.toDtoRes
import org.example.loja_das_coisas_api.repositories.OrderRepository
import org.example.loja_das_coisas_api.repositories.StoreRepository
import org.example.loja_das_coisas_api.repositories.StoreWithdrawRepository
import org.example.loja_das_coisas_api.services.interfaces.StoreFinanceService
import org.springframework.stereotype.Service

@Service
class StoreFinanceServiceImpl(
    private val storeWithdrawService: StoreWithdrawRepository,
    private val orderRepository: OrderRepository,
    private val storeRepository: StoreRepository
): StoreFinanceService {

    @Transactional
    override fun requestWithdraw(storeEmail: String): StoreWithdrawDtoRes {
        val store = storeRepository.findByEmail(storeEmail) ?: throw EntityNotFoundException("Loja indisponível")

        // get store balance
        val storeBalance = orderRepository.getStoreBalance(store.id!!)

        if(storeBalance <= 0){
            throw InsufficientBalanceException()
        }

        // calculate fee
        val feeAmount = ((store.fee.toDouble()/100) * storeBalance).toInt()

        val storeWithdraw = StoreWithdraw(
            store = store,
            amount = storeBalance,
            fee = store.fee,
            feeAmount = feeAmount,
            total = storeBalance - feeAmount,
        )

        // update orders withdrawn status to true
        orderRepository.updateOrdersWithdrawnTrue(store.id!!)

        // save updated orders
        return storeWithdrawService.save(storeWithdraw).toDtoRes()
    }

    override fun getStoreFinanceStatus(storeEmail: String): StoreFinanceStatus {
        val store = storeRepository.findByEmail(storeEmail) ?: throw EntityNotFoundException("Loja indisponível")

        val storeBalance = orderRepository.getStoreBalance(store.id!!)

        val pendingWithdraw = storeWithdrawService.findAllByStoreIdAndStatus(store.id!!, WithdrawStatus.Pending)
            .map { it.toDtoRes() }
        val completedWithdraw = storeWithdrawService.findAllByStoreIdAndStatus(store.id!!, WithdrawStatus.Concluded)
            .map { it.toDtoRes() }

        return StoreFinanceStatus(
            balance = storeBalance,
            pending = pendingWithdraw,
            completed = completedWithdraw
        )
    }
}