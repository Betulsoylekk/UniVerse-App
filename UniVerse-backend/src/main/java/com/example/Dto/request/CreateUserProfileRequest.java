package com.example.Dto.request;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class CreateUserProfileRequest {
    @NonNull
    Long userId;
    @NonNull
    String userName;
    @Nullable
    String bio;

}
