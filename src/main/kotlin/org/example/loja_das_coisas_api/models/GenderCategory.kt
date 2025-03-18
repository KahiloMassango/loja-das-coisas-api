package org.example.loja_das_coisas_api.models

import jakarta.persistence.*
import org.example.loja_das_coisas_api.dtos.sync.GenderCategoryDtoRes
import java.util.UUID

@Entity
@Table(name = "gender_category")
data class GenderCategory(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID,
    @ManyToOne(cascade = [CascadeType.ALL])
    val gender: Gender,
    @ManyToOne(cascade = [CascadeType.ALL])
    val category: Category,
)

fun GenderCategory.toDto(): GenderCategoryDtoRes =
    GenderCategoryDtoRes(
    genderId = gender.id!!,
    categoryId = category.id!!,
)