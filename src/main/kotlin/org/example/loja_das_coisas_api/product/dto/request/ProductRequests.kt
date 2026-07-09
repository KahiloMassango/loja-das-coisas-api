package org.example.loja_das_coisas_api.product.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.web.multipart.MultipartFile
import java.math.BigDecimal
import java.util.*

data class ProductDtoReq(
    @get:NotBlank(message = "O nome do producto é obrigatório")
    val name: String,
    val isAvailable: Boolean,
    val description: String,
    val image: MultipartFile,
    @get:NotNull(message = "O producto deve ter uma categoria")
    val categoryId: UUID,
    val genderId: UUID,
)

data class ProductUpdateDtoReq(
    @get:NotBlank(message = "O nome do producto é obrigatório")
    val name: String,
    val isAvailable: Boolean,
    val description: String,
    val image: MultipartFile?,
)

data class ProductItemDtoReq(
    val stockQuantity: Int,
    val price: BigDecimal,
    val image: MultipartFile,
    val sizeId: UUID?,
    val colorId: UUID?
)

data class ProductItemUpdateDtoReq(
    val stockQuantity: Int,
    val price: BigDecimal,
    val image: MultipartFile?
)

data class ProductFilterDtoReq(
    val maxPrice: Int?,
    val minPrice: Int?,
    val category: String?,
    val gender: String?,
)

data class ProductFilterRequest(
    val gender: String? = null,
    val category: String? = null,
)
