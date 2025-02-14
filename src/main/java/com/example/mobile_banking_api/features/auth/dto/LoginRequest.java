package com.example.mobile_banking_api.features.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

        @NotBlank(message = "Phone Number is required ")
        String phoneNumber,

        @NotBlank(message="Password is required ")
        String password
) {
}
