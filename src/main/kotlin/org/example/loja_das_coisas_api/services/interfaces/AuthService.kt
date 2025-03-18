package org.example.loja_das_coisas_api.services.interfaces

import org.example.loja_das_coisas_api.dtos.requests.LoginDtoRequest

interface AuthService {
    fun login(loginDtoRequest: LoginDtoRequest?): String?
}
