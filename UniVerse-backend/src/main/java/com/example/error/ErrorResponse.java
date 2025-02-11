package com.example.error;

import com.example.error.enums.ErrorType;


import java.time.LocalDateTime;
import java.util.List;

public class ErrorResponse {
    private LocalDateTime timestamp;
    private ErrorType type;
    private String message;
    private List<String> details; // For validation errors

    // Private constructor to enforce the use of the builder
    private ErrorResponse(Builder builder) {
        this.timestamp = builder.timestamp;
        this.type=builder.type;
        this.message = builder.message;
        this.details = builder.details;
    }

    // Static method to initialize the builder
    public static ErrorResponse.Builder builder() {
        return new ErrorResponse.Builder();
    }



    // Getters
    public LocalDateTime getTimestamp() {
        return timestamp;
    }



    public String getMessage() {
        return message;
    }

    public List<String> getDetails() {
        return details;
    }

    // Static Builder Class
    public static class Builder {
        private LocalDateTime timestamp;
        private ErrorType type;
        private String message;
        private List<String> details;

        public ErrorResponse.Builder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public ErrorResponse.Builder type(ErrorType type){
            this.type=type;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public ErrorResponse.Builder details(List<String> details) {
            this.details = details;
            return this;
        }

        public ErrorResponse build() {
            if (this.timestamp == null) {
                this.timestamp = LocalDateTime.now(); // Default timestamp
            }
            return new ErrorResponse(this);
        }
    }
}

