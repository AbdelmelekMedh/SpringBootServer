package com.bezkoder.spring.jwt.mongodb.payload.request;

import jakarta.validation.constraints.NotBlank;

public class LogOutResquest {
    @NotBlank
    private String userId;

    public String getUserId() {
        return userId;
    }

}