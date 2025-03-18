package org.example.loja_das_coisas_api.dtos.sync

import java.util.UUID

data class GenderCategoryDtoRes(
    val genderId: UUID,
    val categoryId: UUID,
)