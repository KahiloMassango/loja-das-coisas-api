package org.example.loja_das_coisas_api.services.interfaces

import org.example.loja_das_coisas_api.dtos.responses.StoreFinanceStatus
import org.example.loja_das_coisas_api.dtos.responses.StoreWithdrawDtoRes

interface StoreFinanceService {
    fun getStoreFinanceStatus(storeEmail: String): StoreFinanceStatus

    fun requestWithdraw(storeEmail: String): StoreWithdrawDtoRes
}