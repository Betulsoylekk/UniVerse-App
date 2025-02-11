package com.example.Controller;

import com.example.Dto.request.LoginRequest;
import com.example.Dto.request.RegisterRequest;
import com.example.Dto.request.ResendVerificationRequest;
import com.example.Dto.response.LoginResponse;
import com.example.Service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("auth/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok("Registration successful! Please verify your email.");
    }
    @PostMapping("/topsecret")
    public ResponseEntity<String> topsecret(@RequestBody LoginRequest request) {
        return ResponseEntity.ok("GERÇEKTEN TOP SECRET!??");
    }


    @PostMapping("auth/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        System.out.println("where are u?");
        String token = authService.authenticate(loginRequest);
        return ResponseEntity.ok(new LoginResponse(token)); // Send token in response
    }

    /*@PostMapping("/api/validate")
    public ResponseEntity<String> validateToken(@RequestBody LoginRequest loginRequest) {
        String token = authService.
        return ResponseEntity.ok(new LoginResponse(token)); // Send token in response
    }*/





    @PostMapping("/resend-verification-email")
    public ResponseEntity<String> resendVerificationEmail(@RequestBody ResendVerificationRequest request) {

            // Validate the email and resend verification email
            authService.sendVerificationEmail(request.getEmail());
            return ResponseEntity.ok("Verification email has been resent.");
        }


    @PostMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam String token) {

            authService.verifyEmailByToken(token); // Assuming you verify the token and mark the email as verified
            return ResponseEntity.ok("Email successfully verified.");
        }
    }




