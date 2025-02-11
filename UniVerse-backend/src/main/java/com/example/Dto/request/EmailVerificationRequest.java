package com.example.Dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class EmailVerificationRequest {
    private String email;
    private String verificationToken;
}
