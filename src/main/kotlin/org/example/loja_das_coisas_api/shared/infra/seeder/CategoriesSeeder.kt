package org.example.loja_das_coisas_api.shared.infra.seeder

import jakarta.transaction.Transactional
import org.example.loja_das_coisas_api.product.model.Category
import org.example.loja_das_coisas_api.product.model.Gender
import org.example.loja_das_coisas_api.product.repository.CategoryRepository
import org.example.loja_das_coisas_api.product.repository.GenderRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class CategoriesSeeder(
    private val categoryRepository: CategoryRepository,
    private val genderRepository: GenderRepository,
) : CommandLineRunner {

    @Transactional
    override fun run(vararg args: String?) {
        if(genderRepository.count() > 0 || categoryRepository.count() > 0) return
        var genders = listOf(
            Gender(name = "Homen"),
            Gender(name = "Mulheres"),
            Gender(name = "Crianças"),
            Gender(name = "Unissex"),
        )

        val categories = listOf(
            Category(
                name = "Roupas",
                hasSizeVariation = true,
                hasColorVariation = true,
                deleted = false,
                genders = genders.toMutableList(),
            ),
            Category(
                name = "Perfumes",
                hasSizeVariation = true,
                hasColorVariation = true,
                deleted = false,
                genders = genders.toMutableList(),
            ),
            Category(
                name = "Calças",
                hasSizeVariation = true,
                hasColorVariation = true,
                deleted = false,
                genders = genders.toMutableList(),
            ),
            Category(
                name = "Calçados",
                hasSizeVariation = true,
                hasColorVariation = true,
                deleted = false,
                genders = genders.toMutableList(),
            )
        )

        genderRepository.saveAllAndFlush(genders)
        categoryRepository.saveAllAndFlush(categories)
    }
}
