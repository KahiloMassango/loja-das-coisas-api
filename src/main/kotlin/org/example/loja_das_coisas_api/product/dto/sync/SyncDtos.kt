package org.example.loja_das_coisas_api.product.dto.sync

import org.example.loja_das_coisas_api.product.dto.response.CategoryDtoRes
import org.example.loja_das_coisas_api.product.dto.response.ColorDtoRes
import org.example.loja_das_coisas_api.product.dto.response.GenderDtoRes
import org.example.loja_das_coisas_api.product.dto.response.SizeDtoRes
import java.util.UUID

data class GenderCategoryDtoRes(
    val genderId: UUID,
    val categoryId: UUID,
)

data class LastUpdatedDtoRes(
    val lastUpdated: Long
)

data class SyncMetadataDtoRes(
    val categories: List<CategoryDtoRes>,
    val genders: List<GenderDtoRes>,
    val genderCategoryRelations: List<GenderCategoryDtoRes>,
    val colors: List<ColorDtoRes>,
    val sizes: List<SizeDtoRes>
)
