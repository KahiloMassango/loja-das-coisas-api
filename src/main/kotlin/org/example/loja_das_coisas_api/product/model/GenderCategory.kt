package org.example.loja_das_coisas_api.product.model

import jakarta.persistence.*
import org.example.loja_das_coisas_api.product.dto.sync.GenderCategoryDtoRes
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
