package org.example.loja_das_coisas_api.repositories

import org.example.loja_das_coisas_api.models.ProductItem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ProductItemRepository : JpaRepository<ProductItem, UUID> {
    fun findByIdAndDeletedFalse(id: UUID): Optional<ProductItem>

    fun findByIdAndProductId(id: UUID, productId: UUID): ProductItem?
    fun findAllByProductIdAndDeletedFalse(productId: UUID): List<ProductItem>

    @Query("""
        SELECT MIN(pi.price)
        FROM ProductItem pi 
        WHERE pi.product.id = :productId AND pi.stockQuantity > 0 AND pi.deleted = false 
    """)
    fun findMinPriceByProductId(productId: UUID): Int

    fun findAllByProductIdAndStockQuantityIsGreaterThanAndDeletedFalse(productId: UUID, stockQuantity: Int = 0): List<ProductItem>

    @Modifying
    @Query("update ProductItem set deleted = true where product.id = :productId")
    fun deleteAllByProductId(productId: UUID)
}
