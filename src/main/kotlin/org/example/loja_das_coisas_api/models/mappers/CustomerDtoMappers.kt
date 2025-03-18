package org.example.loja_das_coisas_api.models.mappers

import org.example.loja_das_coisas_api.dtos.responses.CustomerDtoRes
import org.example.loja_das_coisas_api.models.Customer


fun Customer.toDtoRes() = CustomerDtoRes (
    email = user.email,
    username = username,
    phoneNumber = user.phoneNumber,
)


