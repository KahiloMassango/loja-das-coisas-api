package org.example.loja_das_coisas_api.exceptions

class EntityNotFoundException : Exception {
    constructor() : super("Não Encontrada!")
    constructor(message: String) : super(message)
    constructor(message: String?, cause: Throwable?) : super(message, cause)
}

class AlreadyExistsException(val msg: String): Exception(msg)
