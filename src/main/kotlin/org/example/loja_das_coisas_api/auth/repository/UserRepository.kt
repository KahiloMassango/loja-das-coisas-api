package org.example.loja_das_coisas_api.auth.repository

import org.example.loja_das_coisas_api.auth.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, UUID> {
    @Query("select u from User u where u.email = :identifier or u.phoneNumber = :identifier")
    fun findByEmailOrPhoneNumberAndDeletedFalse(identifier: String): User?

    fun findByEmailOrPhoneNumberAndDeletedFalse(email: String, phoneNumber: String): User?

    fun findByEmailAndDeletedFalse(email: String): User?
}
