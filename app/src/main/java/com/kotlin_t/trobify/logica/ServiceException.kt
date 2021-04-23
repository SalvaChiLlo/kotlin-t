package com.kotlin_t.trobify.logica

class ServiceException: Exception {
    constructor(message: String): super(message)
    constructor(message: String,exception: Exception ): super(message, exception)
}