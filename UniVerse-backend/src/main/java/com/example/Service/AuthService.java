package com.example.Service;

import com.example.Dto.request.LoginRequest;
import com.example.Dto.request.RegisterRequest;

public interface AuthService {
     String authenticate(LoginRequest loginRequest);
     void register(RegisterRequest registerRequest);
     void sendVerificationEmail(String email);
     boolean validateToken(Long userId,String token);

     void verifyEmailByToken(String token);
}
