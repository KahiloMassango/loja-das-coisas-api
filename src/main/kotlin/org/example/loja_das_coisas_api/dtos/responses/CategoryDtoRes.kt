package org.example.loja_das_coisas_api.dtos.responses

import java.util.*


data class CategoryDtoRes(
     val id: UUID,
     val name: String,
     val hasSizeVariation: Boolean,
     val hasColorVariation: Boolean
)

