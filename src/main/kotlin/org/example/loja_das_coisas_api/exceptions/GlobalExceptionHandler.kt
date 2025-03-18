package org.example.loja_das_coisas_api.exceptions

import org.example.loja_das_coisas_api.common.APIResponse
import org.example.loja_das_coisas_api.common.Response
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
    // Handle IllegalArgumentException with BAD_REQUEST and Portuguese message
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(ex: IllegalArgumentException): ResponseEntity<Response<Unit>> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(Response(false, "Argumento inválido: ${ex.message }"))
    }

    // Handle AlreadyExistsException
    @ExceptionHandler(AlreadyExistsException::class)
    protected fun handleAlreadyExistsException(exception: AlreadyExistsException): ResponseEntity<Response<Unit>> {
        val response = APIResponse.error(exception.message!!)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response)
    }

    // Handle EntityNotFoundException with NOT_FOUND and localized message
    @ExceptionHandler(EntityNotFoundException::class)
    protected fun handleEntityNotFoundException(exception: EntityNotFoundException): ResponseEntity<Response<Unit>> {
        val response = APIResponse.error(exception.message!!)
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response)
    }

    // Handle InsufficientStockException
    @ExceptionHandler(InsufficientStockException::class)
    protected fun handleInsufficientStockException(exception: InsufficientStockException): ResponseEntity<Response<Unit>> {
        val response = APIResponse.error(exception.message!!)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response)
    }

    // Handle InsufficientBalanceException
    @ExceptionHandler(InsufficientBalanceException::class)
    protected fun handleInsufficientBalanceException(exception: InsufficientBalanceException): ResponseEntity<Response<Unit>> {
        val response = APIResponse.error(exception.message!!)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response)
    }

    // Handle InvalidCredentialsException
    @ExceptionHandler(InvalidCredentialsException::class)
    protected fun handleInvalidCredentialsException(exception: InvalidCredentialsException): ResponseEntity<Response<Unit>> {
        val response = APIResponse.error(exception.message!!)
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response)
    }

    // Handle DifferentStoreException
    @ExceptionHandler(DifferentStoreProductsException::class)
    protected fun handleDifferentStoreException(exception: DifferentStoreProductsException): ResponseEntity<Response<Unit>> {
        val response = APIResponse.error(exception.message!!)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response)
    }

    // Handle InvalidRefreshTokenException
    @ExceptionHandler(InvalidRefreshTokenException::class)
    protected fun handleInvalidRefreshTokenException(exception: InvalidRefreshTokenException): ResponseEntity<Response<Unit>> {
        val response = APIResponse.error(exception.message!!)
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response)
    }

    // Handle Conflict Exception with CONFLICT status
    @ExceptionHandler(Conflict::class)
    protected fun handleConflictException(exception: HttpClientErrorException): ResponseEntity<Response<Unit>> {
        val response = APIResponse.error("Conflito: " + exception.message)
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response)
    }

    // Handle message not readable (malformed JSON) with UNPROCESSABLE_ENTITY status
    @ExceptionHandler(HttpMessageNotReadableException::class)
    protected fun handleMessageNotReadableException(exception: HttpMessageNotReadableException?): ResponseEntity<Response<Unit>> {
        val response = APIResponse.error("Erro de leitura do corpo da mensagem. Verifique o formato dos dados.")
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response)
    }

    // Handle internal server errors
    @ExceptionHandler(ServerErrorException::class)
    protected fun handleServerErrorException(exception: ServerErrorException): ResponseEntity<Response<Unit>> {
        val response =
            APIResponse.error("Erro no servidor: " + exception.message)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response)
    }

    // Handle validation errors (e.g., @Valid/@NotNull) and return BAD_REQUEST
    @ExceptionHandler(MethodArgumentNotValidException::class)
    protected fun handleValidationException(ex: MethodArgumentNotValidException): ResponseEntity<Response<Unit>> {
        // Cria a resposta de erro consolidada
        val response = APIResponse.error("Erro de validação + ${ex.message}")
        // Retorna a resposta com status 400 (BAD_REQUEST)
        return ResponseEntity.badRequest().body(response)
    }

    // Handle any other unhandled exception
    @ExceptionHandler(Exception::class)
    protected fun handleGenericException(exception: Exception): ResponseEntity<Response<Unit>> {
        val response =
            APIResponse.error(exception.message!!)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response)
    }
}
