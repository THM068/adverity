package com.adverity.exceptions

class BusinessException extends RuntimeException {
    String message

    BusinessException(String message) {
        super(message)
    }


}
