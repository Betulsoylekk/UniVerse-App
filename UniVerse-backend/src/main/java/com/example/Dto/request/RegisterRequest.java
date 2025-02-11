package com.example.Dto.request;


import com.example.validation.ValidEmail;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Getter
@NoArgsConstructor
public class RegisterRequest {
    @NonNull
    @ValidEmail
    private String email;
    @NonNull
    private String password;
    @NonNull
    private String fullName;
    @NonNull
    private String collegeName;

}

