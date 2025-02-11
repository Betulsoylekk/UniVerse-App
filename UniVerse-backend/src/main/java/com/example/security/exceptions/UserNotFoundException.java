package com.example.security.exceptions;

import com.example.error.enums.ErrorType;
import lombok.Getter;

@Getter
public class UserNotFoundException extends RuntimeException {
    private final ErrorType errorType;
    private final Long userId; // Additional context

    public UserNotFoundException(Long userId) {
        super("User not found with ID: " + userId);
        this.errorType = ErrorType.USER_NOT_FOUND;
        this.userId = userId;
    }
    public UserNotFoundException() {
        super("User not found with given token.");
        this.errorType = ErrorType.USER_NOT_FOUND;
        this.userId=null;
    }
}
