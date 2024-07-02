package com.example.mobile_banking_api.features.accountType;
import com.example.mobile_banking_api.domain.AccountType;
import com.example.mobile_banking_api.features.accountType.dto.AccountTypeRequest;
import com.example.mobile_banking_api.features.accountType.dto.AccountTypeResponse;
import com.example.mobile_banking_api.features.accountType.dto.AccountTypeUpdateRequest;
import com.example.mobile_banking_api.mapper.AccountTypeMapper;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class AccountTypeServiceImpl  implements AccountTypeService {
    private final  AccountTypeRepository  accountTypeRepository;
    private final AccountTypeMapper accountTypeMapper;

    @Override
    public AccountTypeResponse findByAlias(String alias) {

        // Validate alias
        AccountType  accountType = accountTypeRepository
                .findByAlias(alias)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Account type alias has not been found"
                ));
        return accountTypeMapper.toAccountTypeResponse(accountType);
    }

    @Override
    public void deleteByAlias(String alias) {

        // Validate alias
        AccountType accountType  = accountTypeRepository
                .findByAlias(alias)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Account type alias has not been found "));
             accountTypeRepository.delete(accountType);
    }

    @Override
    public AccountTypeResponse updateByAlias(String alias, AccountTypeUpdateRequest accountTypeUpdateRequest) {

        // validate alias
        AccountType accountType  = accountTypeRepository
                .findByAlias(alias)
                .orElseThrow(
                        ()-> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Account type alias has not been found "));

        log.info("Before Map :{},{},{}",accountType.getId(),accountType.getDescription(), accountType.getIsDeleted());
          accountTypeMapper.fromAccountUpdateRequest(accountTypeUpdateRequest,accountType);
        log.info("After Map :{},{},{}",accountType.getId(),accountType.getDescription(), accountType.getIsDeleted());

       accountType =  accountTypeRepository.save(accountType);

        return accountTypeMapper.toAccountTypeResponse(accountType);
    }

    @Override
    public void createNew(AccountTypeRequest accountTypeRequest) {


        // Validate Alias
        if (accountTypeRepository.existsByAlias(accountTypeRequest.alias())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Account type alias has already existed");
        }

        AccountType  accountType =  accountTypeMapper.fromAccountTypeRequest(accountTypeRequest);
        accountType.setIsDeleted(false);
         accountTypeRepository.save(accountType);
    }

    @Override
    public List<AccountTypeResponse> findList() {

        Sort sortById = Sort.by(Sort.Direction.DESC, "id");
            List<AccountType>  accountType = accountTypeRepository.findAll(sortById);

        return accountTypeMapper.toAccountTypeResponseList(accountType);
    }
}
