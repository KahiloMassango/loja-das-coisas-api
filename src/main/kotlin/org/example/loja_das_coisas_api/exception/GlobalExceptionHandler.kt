package org.example.loja_das_coisas_api.exception

import org.example.loja_das_coisas_api.shared.dto.APIResponse
import org.example.loja_das_coisas_api.shared.dto.Response
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.HttpClientErrorException.Conflict
import org.springframework.web.server.ServerErrorException

@ControllerAdvice
class GlobalExceptionHandler<T> {

    @ExceptionHandler(BusinessException::class)
    fun handleBusinessException(ex: BusinessException): ResponseEntity<Response<Unit>> {
        val response = APIResponse.error(ex.message)
        return ResponseEntity.status(ex.status).body(response)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(ex: IllegalArgumentException): ResponseEntity<Response<Unit>> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(Response(false, "Argumento inválido: ${ex.message}"))
    }

    @ExceptionHandler(Conflict::class)
    protected fun handleConflictException(exception: HttpClientErrorException): ResponseEntity<Response<Unit>> {
        val response = APIResponse.error("Conflito: " + exception.message)
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    protected fun handleMessageNotReadableException(exception: HttpMessageNotReadableException?): ResponseEntity<Response<Unit>> {
        val response = APIResponse.error("Erro de leitura do corpo da mensagem. Verifique o formato dos dados.")
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response)
    }

    @ExceptionHandler(ServerErrorException::class)
    protected fun handleServerErrorException(exception: ServerErrorException): ResponseEntity<Response<Unit>> {
        val response = APIResponse.error("Erro no servidor: " + exception.message)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    protected fun handleValidationException(ex: MethodArgumentNotValidException): ResponseEntity<Response<Unit>> {
        val response = APIResponse.error("Erro de validação: ${ex.bindingResult.fieldError?.defaultMessage ?: ex.message}")
        return ResponseEntity.badRequest().body(response)
    }

    @ExceptionHandler(Exception::class)
    protected fun handleGenericException(exception: Exception): ResponseEntity<Response<Unit>> {
        val response = APIResponse.error(exception.message ?: "Erro desconhecido")
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response)
    }
}
