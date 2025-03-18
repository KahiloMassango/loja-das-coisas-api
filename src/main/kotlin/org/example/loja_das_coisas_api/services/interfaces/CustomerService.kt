package org.example.loja_das_coisas_api.services.interfaces

import org.example.loja_das_coisas_api.dtos.requests.CustomerDtoReq
import org.example.loja_das_coisas_api.dtos.requests.CustomerUpdateDtoReq
import org.example.loja_das_coisas_api.dtos.responses.CustomerDtoRes

interface CustomerService {
    fun getDetails(email: String): CustomerDtoRes
    fun create(request: CustomerDtoReq): CustomerDtoRes
    fun delete(email: String)
    fun update(email: String, request: CustomerUpdateDtoReq): CustomerDtoRes
    fun updatePassword(identifier: String, newPassword: String)
}
