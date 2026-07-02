package org.example.loja_das_coisas_api.store.repository

import org.example.loja_das_coisas_api.store.model.StoreProfile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface StoreRepository : JpaRepository<StoreProfile, UUID> {
    fun findByStoreNameIsAndDeletedFalse(name: String): Optional<StoreProfile>

    fun findByIdAndActiveIsTrueAndDeletedFalse(storeId: UUID): Optional<StoreProfile>

    @Query("select s from StoreProfile s inner join User u on u.id = s.user.id where u.email = :email and s.active = true")
    fun findByEmail(email: String): StoreProfile?
}
