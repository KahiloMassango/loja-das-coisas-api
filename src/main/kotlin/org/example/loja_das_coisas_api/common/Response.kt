package org.example.loja_das_coisas_api.common

data class Response<T>(
    val isSuccess: Boolean,
    val message: String,
    val data: T? = null,
)


object APIResponse {
    fun <T> success(data: T) =
        Response(true, data = data, message = "success")

    fun error(message: String) =
        Response(false, data = Unit, message = message)
}
