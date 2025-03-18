package org.example.loja_das_coisas_api.repositories

import org.example.loja_das_coisas_api.models.Product
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ProductRepository : JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {
    fun findByNameAndDeletedFalse(name: String): Optional<Product>

    fun findByIdAndDeletedFalse(id: UUID): Optional<Product>

    fun findByIdAndAvailableTrueAndDeletedFalse(id: UUID): Optional<Product>

    fun findAllByStoreIdAndDeletedFalse(storeId: UUID): List<Product>

    fun findByIdAndStoreIdAndDeletedFalse(id: UUID, storeId: UUID): Optional<Product>

    fun findAllByGenderIdAndCategoryIdAndDeletedFalse(genderId: UUID, categoryId: UUID): List<Product>

    override fun findAll(spec: Specification<Product>?): MutableList<Product>

}
