package org.example.loja_das_coisas_api.customer.repository

import org.example.loja_das_coisas_api.customer.model.CustomerProfile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CustomerRepository : JpaRepository<CustomerProfile, UUID> {
    fun findByIdAndDeletedFalse(id: UUID): Optional<CustomerProfile>
    fun findByUserIdAndDeletedFalse(userId: UUID): CustomerProfile?

    @Query("select c from CustomerProfile c inner join User u on u.id = c.user.id where u.email = :email")
    fun findByEmail(email: String): CustomerProfile?
}
