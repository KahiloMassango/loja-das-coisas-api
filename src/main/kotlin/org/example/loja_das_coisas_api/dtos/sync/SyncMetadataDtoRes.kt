package org.example.loja_das_coisas_api.dtos.sync

import org.example.loja_das_coisas_api.dtos.responses.CategoryDtoRes
import org.example.loja_das_coisas_api.dtos.responses.ColorDtoRes
import org.example.loja_das_coisas_api.dtos.responses.GenderDtoRes
import org.example.loja_das_coisas_api.dtos.responses.SizeDtoRes

data class SyncMetadataDtoRes(
    val categories: List<CategoryDtoRes>,
    val genders: List<GenderDtoRes>,
    val genderCategoryRelations: List<GenderCategoryDtoRes>,
    val colors: List<ColorDtoRes>,
    val sizes: List<SizeDtoRes>
)
