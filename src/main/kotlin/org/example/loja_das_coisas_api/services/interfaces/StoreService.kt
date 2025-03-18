package org.example.loja_das_coisas_api.services.interfaces

import org.example.loja_das_coisas_api.dtos.requests.StoreDtoReq
import org.example.loja_das_coisas_api.dtos.responses.StoreAdminDtoRes
import org.example.loja_das_coisas_api.dtos.responses.StoreDetailDtoRes
import org.example.loja_das_coisas_api.dtos.responses.StoreDtoRes
import java.util.*


interface StoreService {
    fun createStore(request: StoreDtoReq): StoreDtoRes

    fun getStoreById(id: UUID): StoreDetailDtoRes

    fun storeDetails(email: String): StoreDetailDtoRes

    fun updateStore(email: String, request: StoreDtoReq): StoreDtoRes

    fun updateStoreStatus(storeId: UUID, isActive: Boolean)

    fun deleteStore(email: String)

    fun getAllStores(): List<StoreAdminDtoRes>
}
