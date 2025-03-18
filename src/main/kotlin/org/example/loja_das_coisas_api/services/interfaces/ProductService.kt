package org.example.loja_das_coisas_api.services.interfaces

import org.example.loja_das_coisas_api.dtos.requests.ProductDtoReq
import org.example.loja_das_coisas_api.dtos.requests.ProductFilterDtoReq
import org.example.loja_das_coisas_api.dtos.requests.ProductUpdateDtoReq
import org.example.loja_das_coisas_api.dtos.responses.ProductDtoRes
import org.example.loja_das_coisas_api.dtos.responses.ProductStoreDtoRes
import org.example.loja_das_coisas_api.dtos.responses.ProductWithVariationDtoRes
import org.example.loja_das_coisas_api.dtos.responses.ProductWithVariationStoreDtoRes
import java.util.*

interface ProductService {
    // Store
    fun createProduct(storeEmail: String, request: ProductDtoReq): ProductStoreDtoRes
    fun updateProduct(storeEmail: String, productId: UUID, request: ProductUpdateDtoReq): ProductStoreDtoRes
    fun getProductByIdAndStoreId(storeEmail: String, productId: UUID): ProductWithVariationStoreDtoRes
    fun deleteProduct(storeEmail: String, productId: UUID)
    fun getStoreProducts(storeEmail: String): List<ProductStoreDtoRes>

    fun filterForStore(storeEmail: String, gender: String?, category: String?): List<ProductStoreDtoRes>

    // Customer
    fun getProductById(id: UUID): ProductWithVariationDtoRes
    fun getProductsByFilter(filter: ProductFilterDtoReq): List<ProductDtoRes>
    fun search(query: String): List<ProductDtoRes>
    fun filterProductsByGenderAndCategory(gender: String?, category: String?): List<ProductDtoRes>
    fun getProductsByStoreId(storeId: UUID): List<ProductDtoRes>


}
