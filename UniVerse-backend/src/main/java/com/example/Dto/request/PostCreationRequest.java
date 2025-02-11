package com.example.Dto.request;

import lombok.Getter;

@Getter
public class PostCreationRequest {
    private Long userProfileId;
    private String topic;
    private String content;



}
