package org.example.loja_das_coisas_api.store.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.example.loja_das_coisas_api.product.dto.response.ProductDtoRes
import java.math.BigDecimal
import java.util.*

data class StoreRegisterRequest(
    @field:NotBlank
    val email: String,
    @field:NotBlank
    @field:Size(min = 9, max = 9)
    val phoneNumber: String,
    val password: String,
    @get:NotBlank(message = "O nome da loja é obrigatório")
    @get:Size(max = 100, message = "O nome da loja não pode ter mais que 100 caracteres")
    val storeName: String,
    @get:NotBlank(message = "NIF obrigatório")
    var nif: String,
    var offersDelivery: Boolean,
    @get:NotBlank(message = "A imagem da loja é obrigatória")
    val storeLogoUrl: String,
    @get:NotBlank(message = "A descrição da loja é obrigatória")
    @get:Size(max = 200, message = "A descrição da loja não pode ter mais que 200 caracteres")
    val description: String,
    @get:NotNull(message = "A latitude é obrogatória")
    val latitude: Double,
    @get:NotNull(message = "A longitude é obrigatória")
    val longitude: Double,
    @get:NotBlank(message = "Hora de inválida")
    val openingTime: String,
    @get:NotBlank(message = "Hora de inválida")
    val closingTime: String,
    @get:NotBlank(message = "A imagem da loja é obrigatória")
    val address: String,
    @get:NotBlank(message = "Nome da conta bancária é obrigatório")
    val bankAccountName: String,
    @get:NotBlank(message = "Iban é obrigatório")
    val bankAccountIban: String,
    val fee: Double,
)

data class StoreDtoRes(
    var id: UUID,
    var storeName: String,
    var storeLogoUrl: String,
    var description: String,
    var nif: String,
    var latitude: Double,
    var longitude: Double,
    var offersDelivery: Boolean,
)

data class StoreAdminDtoRes(
    val id: UUID,
    val name: String,
    val email: String,
    val phoneNumber: String,
    val logoUrl: String,
    val description: String,
    val offersDelivery: Boolean,
    val nif: String,
    val isActive: Boolean,
    val memberSince: String,
    val openingTime: String,
    val closingTime: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
)

data class StoreDetailDtoRes(
    val name: String,
    val logoUrl: String,
    val description: String,
    val offersDelivery: Boolean,
    val nif: String,
    val memberSince: String,
    val openingTime: String,
    val closingTime: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val totalProducts: Int,
    val totalCompletedOrders: Int,
    val products: List<ProductDtoRes>
)

data class StoreFinanceStatus(
    val balance: BigDecimal,
    val pending: List<StoreWithdrawDtoRes>,
    val completed: List<StoreWithdrawDtoRes>
)

data class StoreWithdrawDtoRes(
    val amount: BigDecimal,
    val fee: Double,
    val feeAmount: BigDecimal,
    val total: BigDecimal,
    val requestDate: String
)
