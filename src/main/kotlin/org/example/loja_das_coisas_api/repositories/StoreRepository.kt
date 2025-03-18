package org.example.loja_das_coisas_api.repositories

import org.example.loja_das_coisas_api.models.store.Store
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface StoreRepository : JpaRepository<Store, UUID> {
    fun findByStoreNameIsAndDeletedFalse(name: String): Optional<Store>

    fun findByIdAndActiveIsTrueAndDeletedFalse(storeId: UUID): Optional<Store>

    @Query("select s from Store s inner join User u on u.id = s.user.id where u.email = :email and s.active = true")
    fun findByEmail(email: String): Store?


}
