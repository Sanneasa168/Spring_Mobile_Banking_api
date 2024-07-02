package com.example.mobile_banking_api.features.account.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record AccountTranasferLimitRequest(
        @NotNull(message = "Amount is rquired")
        @Min(1000)
        BigDecimal amount
) {
}
