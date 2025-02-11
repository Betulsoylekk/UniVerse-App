package com.example.Dto.request;

import com.example.validation.ValidEmail;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LoginRequest {
    @NonNull
    @ValidEmail
    private String email;
    @NonNull
    private String password;
}
