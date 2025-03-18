package org.example.loja_das_coisas_api.services.interfaces

import org.example.loja_das_coisas_api.dtos.requests.ProductItemDtoReq
import org.example.loja_das_coisas_api.dtos.requests.ProductItemUpdateDtoReq
import org.example.loja_das_coisas_api.dtos.responses.ProductItemDtoRes
import org.example.loja_das_coisas_api.dtos.responses.ProductItemStoreDtoRes
import java.util.*


interface ProductItemService {
    fun create(productId: UUID, request: ProductItemDtoReq): ProductItemStoreDtoRes
    fun update(productId: UUID, productItemId: UUID, request: ProductItemUpdateDtoReq): ProductItemDtoRes
    fun delete(productId: UUID, productItemId: UUID, storeEmail: String)
}