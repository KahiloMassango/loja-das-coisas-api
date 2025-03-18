package org.example.loja_das_coisas_api.repositories

import org.example.loja_das_coisas_api.models.Customer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CustomerRepository : JpaRepository<Customer, UUID> {
    fun findByIdAndDeletedFalse(id: UUID): Optional<Customer>
    fun findByUserIdAndDeletedFalse(userId: UUID): Customer?

    @Query("select c from Customer c inner join User u on u.id = c.user.id where u.email = :email")
    fun findByEmail(email: String): Customer?
}
