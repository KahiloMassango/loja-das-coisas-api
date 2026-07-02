package org.example.loja_das_coisas_api.shared.infra.seeder

import jakarta.annotation.PostConstruct
import org.example.loja_das_coisas_api.product.repository.CategoryRepository
import org.springframework.stereotype.Component

@Component
class CategoriesSeeder(
    private val categoryRepository: CategoryRepository
) {
    @PostConstruct
    fun seedCategories() {
        // Categories seeding logic is commented out in original file
    }
}
