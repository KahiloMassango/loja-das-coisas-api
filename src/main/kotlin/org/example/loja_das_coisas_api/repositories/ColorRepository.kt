package org.example.loja_das_coisas_api.repositories

import org.example.loja_das_coisas_api.models.Color
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ColorRepository: JpaRepository<Color, UUID> {
    fun findByNameEqualsAndDeletedFalse(name: String): Optional<Color>
    fun findByIdAndDeletedFalse(id: UUID): Optional<Color>

    fun findAllByDeletedFalse(): List<Color>

}