package org.example.loja_das_coisas_api.dtos.requests

data class CategoryDtoReq(
    val name: String,
    var hasColorVariations: Boolean,
    var hasSizeVariations: Boolean,
    var genders: List<String>
)
data class CategoryUpdateDtoReq(
    val name: String
)

