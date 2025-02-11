package com.example.error.enums;

import org.springframework.http.HttpStatus;

public enum ErrorType {
    // Common errors
    INTERNAL_SERVER_ERROR(500, "GENERAL", HttpStatus.INTERNAL_SERVER_ERROR),
    BAD_REQUEST(400, "REQUEST", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(400,"AUTH",HttpStatus.NOT_FOUND),

    // Auth-specific errors
    TOKEN_EXPIRED(401, "AUTH", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN(401, "AUTH", HttpStatus.UNAUTHORIZED),
    ACCESS_DENIED(403, "AUTH", HttpStatus.FORBIDDEN),

    // Validation errors
    VALIDATION_ERROR(400, "VALIDATION", HttpStatus.BAD_REQUEST);

    private final int code;
    private final String group; // For categorizing (e.g., "AUTH", "PAYMENT")
    private final HttpStatus httpStatus;


    ErrorType(int code, String group, HttpStatus httpStatus) {
        this.code = code;
        this.group = group;
        this.httpStatus = httpStatus;
    }




    public int getCode() {
        return code;
    }

    public String getGroup() {
        return group;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
