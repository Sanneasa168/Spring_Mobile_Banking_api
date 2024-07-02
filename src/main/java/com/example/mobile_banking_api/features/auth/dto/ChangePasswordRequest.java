package com.example.mobile_banking_api.features.auth.dto;

public record ChangePasswordRequest(

        String oldPassword,

        String password,

        String confirmPassword
) {
}
