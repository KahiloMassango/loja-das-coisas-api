package org.example.loja_das_coisas_api.dtos.responses

import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime



data class ResponseError(
    @get: NotNull(message = "Message can't be null")
    val message:  String,
    @get: NotNull(message = "Timestamp can't be null")
    val timestamp: LocalDateTime

)
