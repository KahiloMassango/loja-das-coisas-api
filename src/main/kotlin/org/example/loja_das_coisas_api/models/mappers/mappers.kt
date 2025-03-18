package org.example.loja_das_coisas_api.models.mappers

import org.example.loja_das_coisas_api.dtos.responses.CategoryDtoRes
import org.example.loja_das_coisas_api.dtos.responses.ColorDtoRes
import org.example.loja_das_coisas_api.dtos.responses.GenderDtoRes
import org.example.loja_das_coisas_api.dtos.responses.SizeDtoRes
import org.example.loja_das_coisas_api.models.Category
import org.example.loja_das_coisas_api.models.Color
import org.example.loja_das_coisas_api.models.Gender
import org.example.loja_das_coisas_api.models.Size

fun Category.toDtoRes(): CategoryDtoRes = CategoryDtoRes(
    id = id!!,
    name = name,
    hasSizeVariation = hasSizeVariation,
    hasColorVariation = hasColorVariation
)

fun Color.toDtoRes() = ColorDtoRes(id = id!!, name = name)

fun Gender.toDtoRes(): GenderDtoRes = GenderDtoRes(
    id = id!!,
    name = name,
)

fun Size.toDtoRes() = SizeDtoRes(id = id!!, value = value, categoryId = category.id!!)
