package org.example.loja_das_coisas_api.product.repository

import org.example.loja_das_coisas_api.product.model.GenderCategory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface GenderCategoryRepository : JpaRepository<GenderCategory, UUID>
