package com.example.mobile_banking_api.features.auth.dto;

import lombok.Builder;

@Builder
public record RegisterResponse(

        String email,

        String message
) {
}
