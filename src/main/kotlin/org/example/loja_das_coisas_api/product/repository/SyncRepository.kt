package org.example.loja_das_coisas_api.product.repository

import org.example.loja_das_coisas_api.product.model.Category
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface SyncRepository : JpaRepository<Category, UUID> {

    @Query(
        value = """
        SELECT COALESCE(EXTRACT(EPOCH FROM MAX(updated_at)), 0 )* 1000 AS last_updated
        FROM (SELECT MAX(updated_at) AS updated_at
        FROM category
        UNION ALL
        SELECT MAX(updated_at) AS updated_at
        FROM genders
        UNION ALL
        SELECT MAX(updated_at) AS updated_at
        FROM color
        UNION ALL
        SELECT MAX(updated_at) AS updated_at
        FROM size) AS combined;
        """,
        nativeQuery = true
    )
    fun getLastUpdated(): Long
}
