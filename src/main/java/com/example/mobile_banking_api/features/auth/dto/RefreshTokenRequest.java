package com.example.mobile_banking_api.features.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(

        @NotBlank( message="Refresh token is required ")
        String refreshToken
) {
}
