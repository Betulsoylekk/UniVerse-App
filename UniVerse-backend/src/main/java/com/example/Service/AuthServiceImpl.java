package com.example.Service;

import com.example.Dto.request.LoginRequest;
import com.example.Dto.request.RegisterRequest;
import com.example.Model.User;
import com.example.Repository.UserRepository;
import com.example.security.JwtUtil;
import com.example.security.exceptions.UserNotFoundException;
import com.example.validation.EmailValidator;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository, EmailService emailService, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.jwtUtil = jwtUtil;
    }

    public String authenticate(LoginRequest loginRequest) {
        // Fetch user by email
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        // Verify password
        if (!BCrypt.checkpw(loginRequest.getPassword(), user.getHashedPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        // Ensure email is verified
        if (user.getIsEmailVerified() == 0) {
            throw new IllegalStateException("Email not verified");
        }

        // Generate and return JWT token
        return jwtUtil.generateToken(user.getEmail());
    }

    @Transactional
    public void register(RegisterRequest request) {

        userRepository.findByEmail(request.getEmail())
                .ifPresent(u -> {
                    throw new IllegalArgumentException("The email is already in use.");
                });

            String hashedPassword = BCrypt.hashpw(request.getPassword(), BCrypt.gensalt());
            String token = UUID.randomUUID().toString();

            User user = new User(
                    request.getEmail(),
                    hashedPassword,
                    request.getFullName(),
                    request.getCollegeName()
            );
            user.setVerificationToken(token);
            user.setCreatedAt(LocalDateTime.now());
            user.setIsEmailVerified(0);

            userRepository.save(user);

            emailService.sendVerificationEmail(user.getEmail(), token);
        }

    @Transactional
    public void sendVerificationEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Check if the user's email is already verified
        if (user.getIsEmailVerified() == 1) {
            throw new IllegalArgumentException("Email already verified");
        }

        // Create or get verification token
        String token = createOrGetVerificationToken(user);

        // Send the verification email asynchronously
        CompletableFuture<Boolean> emailSentFuture = emailService.sendVerificationEmail(email, token);

        // After triggering email send, update user with the last email sent timestamp (independently)
        emailSentFuture.thenAccept(isSent -> {
            if (isSent) {
                user.setLastVerificationEmailSent(token);
                userRepository.save(user);
            } else {
                // You can handle the failure case here if you want, e.g., log it, alert the user, etc.
                System.err.println("Email could not be sent.");
            }
        });
    }

    private String createOrGetVerificationToken(User user) {
        String token = user.getVerificationToken();
        if (token == null) {
            token = UUID.randomUUID().toString();
            user.setVerificationToken(token);
            userRepository.save(user);  // Save the user with the token
        }
        return token;
    }

    @Override
    public boolean validateToken(Long userId, String token) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        String emailToken = user.getVerificationToken();
        if (emailToken.equals(token)) {
            return true;
        }
        return false;
    }


    @Override
    public void verifyEmailByToken(String token) {
        // Logic to find the user by token
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired token"));

        Integer verified = user.getIsEmailVerified();
        if (verified == 1) {

            throw new IllegalArgumentException("The email is already verified.");

        }

        boolean isValidated = validateToken(user.getId(), token);
        if (isValidated) {
            user.setIsEmailVerified(1);
            userRepository.save(user);
        }
    }
}













