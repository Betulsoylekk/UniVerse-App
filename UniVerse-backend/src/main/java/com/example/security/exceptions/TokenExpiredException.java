package com.example.security.exceptions;

import com.example.error.enums.ErrorType;

// Specific exceptions
public class TokenExpiredException extends AuthenticationException {

    public TokenExpiredException() {
        super(ErrorType.TOKEN_EXPIRED, "JWT token has expired");
    }
}
