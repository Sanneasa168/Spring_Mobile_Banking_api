package com.example.mobile_banking_api.features.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record VerificationRequest(
        @NotBlank(message = "Email is required")
        String email,

        @NotBlank(message = "Verify Code  is required")
        String verifiedCode
) {
}
