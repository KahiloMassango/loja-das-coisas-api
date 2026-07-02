package org.example.loja_das_coisas_api.product.repository

import org.example.loja_das_coisas_api.product.model.Gender
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface GenderRepository : JpaRepository<Gender, UUID> {
    fun findByNameEqualsAndDeletedFalse(name: String): Optional<Gender>
    fun findAllByDeletedFalse(): List<Gender>
}
