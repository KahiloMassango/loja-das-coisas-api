package org.example.loja_das_coisas_api.dtos.requests

import java.util.*

data class SizeDtoReq(
    val value: String
)


data class SizeRequest(
    val categoryId: UUID,
    val sizes: List<SizeDtoReq>
)