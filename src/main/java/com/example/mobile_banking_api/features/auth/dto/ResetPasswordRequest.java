package com.example.mobile_banking_api.features.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record ResetPasswordRequest(
        @NotBlank(message = "Email is required ")
        String email
) {
}
