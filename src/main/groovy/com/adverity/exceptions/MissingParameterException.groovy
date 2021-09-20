package com.adverity.exceptions

class MissingParameterException extends BusinessException {
    String message

    MissingParameterException(String message) {
        super(message)
        this.message = message
    }
}
