package com.example.Dto.response;

public class LoginResponse {
    private String token;

    // Constructor
    public LoginResponse(String token) {
        this.token = token;
    }

    // Getter (required for JSON serialization)
    public String getToken() {
        return token;
    }
}