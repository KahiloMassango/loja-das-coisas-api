package org.example.loja_das_coisas_api.repositories

import org.example.loja_das_coisas_api.models.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface UserRepository: JpaRepository<User, UUID> {
    @Query("select u from User u where u.email = :identifier or u.phoneNumber = :identifier")
    fun findByEmailOrPhoneNumberAndDeletedFalse(identifier: String): User?

    fun findByEmailOrPhoneNumberAndDeletedFalse(email: String, phoneNumber: String): User?

    fun findByEmailAndDeletedFalse(email: String): User?
}