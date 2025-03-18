package org.example.loja_das_coisas_api.repositories.util

import jakarta.persistence.criteria.JoinType
import org.example.loja_das_coisas_api.models.Category
import org.example.loja_das_coisas_api.models.Gender
import org.example.loja_das_coisas_api.models.Product
import org.example.loja_das_coisas_api.models.ProductItem
import org.example.loja_das_coisas_api.models.store.Store
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.domain.Specification.where
import java.util.*


class ProductSpecification {

    private fun notDeletedProduct(): Specification<Product> =
        Specification { root, _, cb -> cb.isFalse(root.get("deleted")) }

    private fun nameLike(query: String): Specification<Product> =
        Specification { root, _, cb ->
            val pattern = "%${query.lowercase()}%"  // Match anywhere
            cb.like(cb.lower(root.get("name")), pattern)
        }

    private fun descriptionLike(query: String): Specification<Product> =
        Specification { root, _, cb ->
            val pattern = "%${query.lowercase()}%"  // Match anywhere
            cb.like(cb.lower(root.get("description")), pattern)
        }

    private fun isAvailable(): Specification<Product> {
        return Specification { root, _, cb -> cb.isTrue(root.get("available")) }
    }

    private fun byGenderId(genderId: UUID?): Specification<Product> =
        Specification { root, _, cb ->
            val genderJoin = root.join<Gender, Product>("gender", JoinType.LEFT)
            cb.equal(genderJoin.get<UUID>("id"), genderId)
        }

    private fun storeIsActive(): Specification<Product> =
        Specification { root, _, cb ->
            val storeJoin = root.join<Store, Product>("store", JoinType.LEFT)
            cb.isTrue(storeJoin.get("active"))
        }

    private fun byStoreId(storeId: UUID): Specification<Product> =
        Specification { root, _, cb ->
            val storeJoin = root.join<Store, Product>("store")
            cb.equal(storeJoin.get<UUID>("id"), storeId)
        }

    private fun productItemsNotEmpty(): Specification<Product> {
        return Specification { root, _, cb ->
            val productItemsJoin = root.join<ProductItem, Product>("productItems", JoinType.INNER)
            cb.greaterThan(productItemsJoin.get("stockQuantity"), 0)
        }
    }


    private fun byCategoryId(categoryId: UUID?): Specification<Product> =
        Specification { root, _, cb ->
            val categoryJoin = root.join<Category, Product>("category", JoinType.LEFT)
            cb.equal(categoryJoin.get<UUID>("id"), categoryId)
        }

    fun search(query: String): Specification<Product> {
        val searchSpec = nameLike(query).or(descriptionLike(query))
        return where(notDeletedProduct())
            .and(productItemsNotEmpty())
            .and(isAvailable())
            .and(storeIsActive())
            .and(searchSpec)
    }

    fun filter(genderId: UUID?, categoryId: UUID?): Specification<Product> {
        return where(notDeletedProduct())
            .and(storeIsActive())
            .and(productItemsNotEmpty())
            .and(isAvailable())
            .and(genderId?.let { byGenderId(it) })
            .and(categoryId?.let { byCategoryId(it) })
    }

    fun filterForStore(storeId: UUID, genderId: UUID?, categoryId: UUID?): Specification<Product> {
        return where(notDeletedProduct())
            .and(byStoreId(storeId))
            .and(genderId?.let { byGenderId(it) })
            .and(categoryId?.let { byCategoryId(it) })
    }

    fun allActiveStoreProducts(storeId: UUID): Specification<Product> {
        return where(notDeletedProduct())
            .and(productItemsNotEmpty())
            .and(isAvailable())
            .and(byStoreId(storeId))
    }
}
