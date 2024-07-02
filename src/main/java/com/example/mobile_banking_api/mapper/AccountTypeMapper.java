package com.example.mobile_banking_api.mapper;

import com.example.mobile_banking_api.domain.AccountType;
import com.example.mobile_banking_api.features.accountType.dto.AccountTypeRequest;
import com.example.mobile_banking_api.features.accountType.dto.AccountTypeResponse;
import com.example.mobile_banking_api.features.accountType.dto.AccountTypeUpdateRequest;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface  AccountTypeMapper {

        AccountTypeResponse toAccountTypeResponse(AccountType accountType);

        // Partially map
        @BeanMapping(nullValuePropertyMappingStrategy= NullValuePropertyMappingStrategy.IGNORE)
        void fromAccountUpdateRequest(AccountTypeUpdateRequest accountTypeUpdateRequest,
                                     @MappingTarget AccountType accountType);

        AccountType  fromAccountUpdateRequest(AccountTypeUpdateRequest accountTypeUpdateRequest);

        AccountType  fromAccountTypeRequest(AccountTypeRequest accountTypeRequest);

        List<AccountTypeResponse> toAccountTypeResponseList(List<AccountType> accountType);

}
