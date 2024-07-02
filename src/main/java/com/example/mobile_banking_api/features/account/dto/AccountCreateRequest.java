package com.example.mobile_banking_api.features.account.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
public record AccountCreateRequest (
        @NotBlank(message = "Account is not required")
        String actNo,

        @NotNull(message = " balance is required")
        @Positive
        BigDecimal balance,

        @NotBlank(message = "Account Type is required")
        String accountTypeAlias,

        @NotBlank(message = "Account owner is required")
        String userUuid
) {

}
