package org.example.loja_das_coisas_api.repositories

import org.example.loja_das_coisas_api.models.Size
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface SizeRepository: JpaRepository<Size, UUID> {
    fun findByValueAndCategoryIdAndDeletedFalse(value: String, categoryId: UUID): Optional<Size>
    fun findByValueAndDeletedFalse(value: String): Optional<Size>
    fun findByIdAndCategoryIdAndDeletedFalse(id: UUID, categoryId: UUID): Optional<Size>
    fun findByCategoryIdAndDeletedFalse(categoryId: UUID): List<Size>
    fun findAllByDeletedFalse(): List<Size>
}