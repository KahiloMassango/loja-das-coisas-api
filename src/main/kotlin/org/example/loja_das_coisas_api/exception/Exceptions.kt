package org.example.loja_das_coisas_api.exception

import org.springframework.http.HttpStatus
import java.util.*

class InsufficientStockException(productItemId: UUID, availableStock: Int) :
    BusinessException("Algum producto excedeu a quantidade em estoque!", HttpStatus.BAD_REQUEST)

class DifferentStoreProductsException :
    BusinessException("Produtos de lojas diferentes não são permitidos", HttpStatus.BAD_REQUEST)

class NotAuthorizedException :
    BusinessException("Não autorizado", HttpStatus.UNAUTHORIZED)

class InvalidCredentialsException :
    BusinessException("Credenciais inválidas!", HttpStatus.UNAUTHORIZED)

class InvalidRefreshTokenException :
    BusinessException("Token de actualização inválido", HttpStatus.UNAUTHORIZED)

class InsufficientBalanceException :
    BusinessException("Balanço insuficiente", HttpStatus.BAD_REQUEST)

class TokenExpiredException(message: String) :
    BusinessException(message, HttpStatus.UNAUTHORIZED)

class DeviceTypeNotSetException :
    BusinessException("DeviceType not set", HttpStatus.BAD_REQUEST)
