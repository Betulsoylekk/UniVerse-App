package com.example.security.exceptions;

import com.example.error.enums.ErrorType;

public class InvalidTokenException extends AuthenticationException {
    public InvalidTokenException() {
        super(ErrorType.INVALID_TOKEN, "Invalid JWT token");
    }
}
