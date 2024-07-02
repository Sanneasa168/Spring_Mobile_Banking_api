package com.example.mobile_banking_api.features.accountType.dto;

public record AccountTypeResponse(
        String  alias,
        String name,
        String  description,
        Boolean isDeleted
) {
}
