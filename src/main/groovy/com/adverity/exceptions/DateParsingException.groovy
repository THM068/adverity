package com.adverity.exceptions

class DateParsingException extends BusinessException {
    String message

    DateParsingException(String message) {
        super(message)
        this.message = message
    }
}
