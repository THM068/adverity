package com.adverity.exceptions

class DbAccessException extends BusinessException {
    String message
    DbAccessException(String message) {
        super(message)
        this.message = message
    }
}
