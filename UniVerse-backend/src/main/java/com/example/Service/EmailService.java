package com.example.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;


@Service
public class EmailService {

    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String fromAddress; // Inject the property into a field

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }


    public CompletableFuture<Boolean> sendVerificationEmail(String email, String token) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String verificationUrl = "http://localhost:8080/auth/verify-email?token=" + token;
                String subject = "Verify Your Email Address";
                String body = "Click the link below to verify your email:\n" + verificationUrl;

                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom(fromAddress);
                message.setTo(email);
                message.setSubject(subject);
                message.setText(body);

                mailSender.send(message);
                return true;  // Email sent successfully
            } catch (MailException e) {
                System.err.println("Failed to send email");
                return false;  // Email failed to send
            }
        });
    }
}
