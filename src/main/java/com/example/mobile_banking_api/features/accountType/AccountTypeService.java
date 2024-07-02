package com.example.mobile_banking_api.features.accountType;

import com.example.mobile_banking_api.features.accountType.dto.AccountTypeRequest;
import com.example.mobile_banking_api.features.accountType.dto.AccountTypeResponse;
import com.example.mobile_banking_api.features.accountType.dto.AccountTypeUpdateRequest;

import java.util.List;

public interface AccountTypeService {

    AccountTypeResponse findByAlias(String alias);

    void deleteByAlias(String alias);

    AccountTypeResponse updateByAlias(String alias, AccountTypeUpdateRequest accountTypeUpdateRequest);

    void  createNew(AccountTypeRequest accountTypeRequest);

    List<AccountTypeResponse>   findList();
}
