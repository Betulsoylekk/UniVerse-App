package com.example.error;

import com.example.error.enums.ErrorType;

import com.example.security.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle all unexpected exceptions (fallback)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        ErrorResponse error = ErrorResponse.builder()
                .type(ErrorType.INTERNAL_SERVER_ERROR)
                .message("An unexpected error occurred")
                .build();
   return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
        ErrorType errorType = ex.getErrorType();
        ErrorResponse error = ErrorResponse.builder()
                .type(ErrorType.USER_NOT_FOUND)
                .message("User not found by given userId")
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }







        // Handle validation errors (e.g., @Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        ErrorResponse error = ErrorResponse.builder()
                .type(ErrorType.VALIDATION_ERROR)
                .message("Validation failed")
                .details(errors)
                .build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}