package com.kotlin_t.trobify.persistencia

class ServiceException: Exception {
    constructor(message: String): super(message)
    constructor(message: String,exception: Exception ): super(message, exception)
}