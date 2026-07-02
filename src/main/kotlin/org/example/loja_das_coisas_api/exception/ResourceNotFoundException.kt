package org.example.loja_das_coisas_api.exception

import org.springframework.http.HttpStatus

class ResourceNotFoundException : BusinessException {
    constructor() : super("Não Encontrada!", HttpStatus.NOT_FOUND)
    constructor(message: String) : super(message, HttpStatus.NOT_FOUND)
}

typealias EntityNotFoundException = ResourceNotFoundException

class AlreadyExistsException(msg: String) : BusinessException(msg, HttpStatus.BAD_REQUEST)
