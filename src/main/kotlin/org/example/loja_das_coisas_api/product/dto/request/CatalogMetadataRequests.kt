package org.example.loja_das_coisas_api.product.dto.request

import java.util.*

data class CategoryDtoReq(
    val name: String,
    var hasColorVariations: Boolean,
    var hasSizeVariations: Boolean,
    var genders: List<String>
)

data class CategoryUpdateDtoReq(
    val name: String
)

data class ColorDtoReq(
    val name: String
)

data class GenderDtoReq(
    val name: String,
    val categories: List<String>
)

data class SizeDtoReq(
    val value: String
)

data class SizeRequest(
    val categoryId: UUID,
    val sizes: List<SizeDtoReq>
)
