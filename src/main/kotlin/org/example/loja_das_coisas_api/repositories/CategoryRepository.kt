package org.example.loja_das_coisas_api.repositories

import org.example.loja_das_coisas_api.models.Category
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CategoryRepository : JpaRepository<Category, UUID> {
    fun findByNameIsAndDeletedFalse(name: String): Optional<Category>
    fun findAllByNameInAndDeletedFalse(names: List<String>): List<Category>
    fun findByIdAndDeletedFalse(id: UUID): Optional<Category>
    fun findAllByDeletedFalse(): List<Category>
}
