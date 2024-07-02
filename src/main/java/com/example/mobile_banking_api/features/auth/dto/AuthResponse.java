package com.example.mobile_banking_api.features.auth.dto;

import lombok.Builder;

@Builder
public record AuthResponse(

        // Token type
        String tokenType,

        String accessToken,

        String refreshToken
){
}
