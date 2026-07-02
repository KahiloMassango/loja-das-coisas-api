package org.example.loja_das_coisas_api.shared.infra.seeder

import jakarta.annotation.PostConstruct
import org.example.loja_das_coisas_api.product.model.Gender
import org.example.loja_das_coisas_api.product.repository.GenderRepository
import org.springframework.stereotype.Component

@Component
class GenderSeeder(private val genderRepository: GenderRepository) {
    @PostConstruct
    fun seedSection() {
        val genders = listOf(
            Gender(name = "Homen"),
            Gender(name = "Mulheres"),
            Gender(name = "Crianças"),
            Gender(name = "Unissex"),
        )
        genderRepository.saveAll(genders)
    }
}
