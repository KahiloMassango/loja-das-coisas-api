package org.example.loja_das_coisas_api.dtos.requests

import org.springframework.web.multipart.MultipartFile
import java.util.*

data class ProductItemDtoReq(
    val stockQuantity: Int,
    val price: Int,
    val image: MultipartFile,
    val sizeId: UUID?,
    val colorId: UUID?
)

data class ProductItemUpdateDtoReq(
    val stockQuantity: Int,
    val price: Int,
    val image: MultipartFile?
)
