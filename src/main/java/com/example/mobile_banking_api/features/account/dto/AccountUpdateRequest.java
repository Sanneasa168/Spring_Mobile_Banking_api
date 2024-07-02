package com.example.mobile_banking_api.features.account.dto;

import java.math.BigDecimal;

public record AccountUpdateRequest(
        String actName,
        String actNo,
        BigDecimal balance,
        BigDecimal transferLimit
) {
}
