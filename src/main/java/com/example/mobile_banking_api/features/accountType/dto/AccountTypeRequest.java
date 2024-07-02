package com.example.mobile_banking_api.features.accountType.dto;

import jakarta.validation.constraints.NotBlank;

public record AccountTypeRequest(

        @NotBlank(message="Alias is required ")
        String alias,

        @NotBlank(message="Name is required ")
        String name,

        String description
) {
}
