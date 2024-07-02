package com.example.mobile_banking_api.features.accountType.dto;
public record AccountTypeUpdateRequest(

        String description,

        Boolean isDeleted
) {
}
