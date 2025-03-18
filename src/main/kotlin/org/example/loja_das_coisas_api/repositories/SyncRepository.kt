package org.example.loja_das_coisas_api.repositories

import org.example.loja_das_coisas_api.models.Category
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface SyncRepository : JpaRepository<Category, String> {

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