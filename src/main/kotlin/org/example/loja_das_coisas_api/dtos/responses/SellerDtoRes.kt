package org.example.loja_das_coisas_api.dtos.responses

import java.time.LocalDateTime
import java.util.*


class SellerDtoRes(
    var id: UUID,
    var storeName: String,
    var phoneNumber: String,
    var storeLogoUrl: String,
    var latitude: Double,
    var longitude: Double,
    var email: String,
    var nif: String,
    var createdAt: LocalDateTime,
    var updatedAt: LocalDateTime,
)
