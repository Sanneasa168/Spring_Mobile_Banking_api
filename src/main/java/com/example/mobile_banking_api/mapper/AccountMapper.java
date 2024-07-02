package com.example.mobile_banking_api.mapper;

import com.example.mobile_banking_api.domain.Account;
import com.example.mobile_banking_api.features.account.dto.AccountCreateRequest;
import com.example.mobile_banking_api.features.account.dto.AccountResponse;
import com.example.mobile_banking_api.features.account.dto.AccountUpdateRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {
//    Map Account to AccountResponse
//    Source  = Account
//    Target = AccountResponse

//    @Mapping(source = "accountType.alias",target="accountTypeAlias")
    AccountResponse toAccountResponse(Account account);

    Account fromAccountCreateRequest(AccountCreateRequest accountCreateRequest);
}
