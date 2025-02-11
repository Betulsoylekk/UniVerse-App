package com.example.security.exceptions;

import com.example.error.enums.ErrorType;

// Base exception
public abstract class AuthenticationException extends RuntimeException {
    private final ErrorType errorCode;

    public AuthenticationException(ErrorType errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}

