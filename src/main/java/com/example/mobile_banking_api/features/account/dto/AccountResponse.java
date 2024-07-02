package com.example.mobile_banking_api.features.account.dto;
import com.example.mobile_banking_api.features.accountType.dto.AccountTypeResponse;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record AccountResponse(
        String alias,
        String actName,
        String actNo,
        BigDecimal balance,
           AccountTypeResponse  accountType
//         String accountTypeAlias
) {
}
