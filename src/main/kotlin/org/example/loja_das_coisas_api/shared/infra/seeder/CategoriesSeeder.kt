package org.example.loja_das_coisas_api.shared.infra.seeder

import jakarta.transaction.Transactional
import org.example.loja_das_coisas_api.product.model.Category
import org.example.loja_das_coisas_api.product.model.Gender
import org.example.loja_das_coisas_api.product.repository.CategoryRepository
import org.example.loja_das_coisas_api.product.repository.GenderRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Component
@Order(1)
class CategoriesSeeder(
    private val categoryRepository: CategoryRepository,
    private val genderRepository: GenderRepository,
) : CommandLineRunner {

    @Transactional
    override fun run(vararg args: String?) {
        if(genderRepository.count() > 0 || categoryRepository.count() > 0) return

        val genders = listOf(
            Gender(name = "Homem"),
            Gender(name = "Mulher"),
            Gender(name = "Criança"),
            Gender(name = "Unissex"),
        )
        val savedGenders = genderRepository.saveAllAndFlush(genders)

        val categories = listOf(
            Category(
                name = "Roupas",
                hasSizeVariation = true,
                hasColorVariation = true,
                deleted = false,
                genders = savedGenders.toMutableList(),
            ),
            Category(
                name = "Calçados",
                hasSizeVariation = true,
                hasColorVariation = true,
                deleted = false,
                genders = savedGenders.toMutableList(),
            ),
            Category(
                name = "Acessórios",
                hasSizeVariation = false,
                hasColorVariation = true,
                deleted = false,
                genders = savedGenders.toMutableList(),
            ),
            Category(
                name = "Beleza",
                hasSizeVariation = false,
                hasColorVariation = false,
                deleted = false,
                genders = savedGenders.toMutableList(),
            ),
            Category(
                name = "Eletrônicos",
                hasSizeVariation = false,
                hasColorVariation = true,
                deleted = false,
                genders = savedGenders.toMutableList(),
            ),
            Category(
                name = "Casa",
                hasSizeVariation = false,
                hasColorVariation = true,
                deleted = false,
                genders = savedGenders.toMutableList(),
            )
        )

        categoryRepository.saveAllAndFlush(categories)
    }
}
