package org.example.loja_das_coisas_api.dtos.requests

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class StoreDtoReq(

    @field:NotBlank
    val email: String,
    @field:NotBlank
    @field:Size(min = 9, max = 9)
    val phoneNumber: String,
    val password: String,
    @get: NotBlank(message = "O nome da loja é obrigatório")
    @get: Size(max = 100, message = "O nome da loja não pode ter mais que 100 caracteres")
    val storeName: String,

    @get: NotBlank(message = "NIF obrigatório")
    var nif: String,

    var offersDelivery: Boolean,

    @get: NotBlank(message = "A imagem da loja é obrigatória")
    val storeLogoUrl: String,

    @get: NotBlank(message = "A descrição da loja é obrigatória")
    @get: Size(max = 200, message = "A descrição da loja não pode ter mais que 200 caracteres")
    val description: String,

    @get: NotNull(message = "A latitude é obrogatória")
    val latitude: Double,

    @get: NotNull(message = "A longitude é obrigatória")
    val longitude: Double,

    @get: NotBlank(message = "Hora de inválida")
    val openingTime: String,

    @get: NotBlank(message = "Hora de inválida")
    val closingTime: String,

    @get: NotBlank(message = "A imagem da loja é obrigatória")
    val address: String,
    @get: NotBlank(message = "Nome da conta bancária é obrigatório")
    val bankAccountName: String,
    @get: NotBlank(message = "Iban é obrigatório")
    val bankAccountIban: String,
    val fee: Int,
)
