package org.example.loja_das_coisas_api.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.util.*

class InsufficientStockException(productItemId: UUID, availableStock: Int) :
    Exception("Algum producto excedeu a quantidade em estoque!")

class DifferentStoreProductsException : Exception("Produtos de lojas diferentes não são permitidos")

class NotAuthorizedException : Exception("")

class InvalidCredentialsException : Exception("Credenciais inválidas!")

class InvalidRefreshTokenException : Exception("Token de actualização inválido")

class InsufficientBalanceException : Exception("Balanço insuficiente")

@ResponseStatus(HttpStatus.UNAUTHORIZED)
class TokenExpiredException(message: String) : Exception(message)

