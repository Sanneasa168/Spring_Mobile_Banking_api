package com.example.mobile_banking_api.features.account;
import com.example.mobile_banking_api.domain.Account;
import com.example.mobile_banking_api.domain.AccountType;
import com.example.mobile_banking_api.domain.User;
import com.example.mobile_banking_api.features.account.dto.*;
import com.example.mobile_banking_api.features.accountType.AccountTypeRepository;
import com.example.mobile_banking_api.features.user.UserRepository;
import com.example.mobile_banking_api.mapper.AccountMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImp implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountTypeRepository accountTypeRepository;
    private final UserRepository userRepository;
    private final AccountMapper accountMapper;
//    private final AccountUpdateRequest accountUpdateRequest;


//    @Override
//    public void daleteAccount(String actNo) {
//        Account deleteAccount= accountRepository
//                .findByActNo(actNo)
//                .orElseThrow(()-> new ResponseStatusException(
//                        HttpStatus.NOT_FOUND,
//                        "Account  has not  been found"
//                ));
//                accountRepository.delete(deleteAccount);
//    }

    @Override
    public void isDeleted(String actNo) {
        Account accountIsDeleted = accountRepository
                .findByActNo(actNo)
                .orElseThrow(()-> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Account  has not  been found"
        ));
        accountIsDeleted.setIsDeleted(false);
        accountRepository.delete(accountIsDeleted);
    }
    @Override

        public void updateTransferlimit(String actNo, AccountTranasferLimitRequest accountTranasferLimitRequest) {

        // Validate Account
        Account  account = accountRepository
                .findByActNo(actNo)
                .orElseThrow(()-> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Account type has not  been found"
                ));
        account.setTransferLimit(accountTranasferLimitRequest.amount());
        accountRepository.save(account);
    }
    @Override
    public void hindeAccount(String actNo) {

        // Validate  account
        Account  account = accountRepository
                .findByActNo(actNo)
                .orElseThrow(()-> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Account type has not  been found"
                ));
        account.setIsHidden(true);
        accountRepository.save(account);
    }

    @Override
    public AccountResponse renameAccount(String actNo, AccountRenameRequest accountRenameRequest) {

        // Validate  account
        Account  account = accountRepository
                .findByActNo(actNo)
                .orElseThrow(()-> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Account  has not  been found"
                ));
        account.setAlias(accountRenameRequest.alias());
        account = accountRepository.save(account);

        return  accountMapper.toAccountResponse(account);
    }

    @Override
    public AccountResponse createNew(AccountCreateRequest accountCreateRequest) {

        // Validate account type
        AccountType accountType = accountTypeRepository
                .findByAlias(accountCreateRequest.accountTypeAlias())
                .orElseThrow(()-> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Account type has not  been found"
                ));

        // Validate user
        User user =  userRepository
                .findByUuid(accountCreateRequest.userUuid())
                .orElseThrow(()-> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User has not  been found"
                ));

        //Validate account  no
        if(accountRepository.existsByactNo(accountCreateRequest.actNo())){
            throw new ResponseStatusException
                    (HttpStatus.CONFLICT,
                            "Account no has already been existed ");
        }

        // Validate Balance
        if(accountCreateRequest.balance().compareTo(BigDecimal.valueOf(10))<0){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Balance 10$ is required  to create a account"
            );

        }

        // Transfer DTO to domain Model
        Account account = accountMapper.fromAccountCreateRequest(accountCreateRequest);
        account.setAccountType(accountType);
        account.setUser(user);

        // System generate data
        account.setActName(user.getName());
        account.setIsHidden(false);
        account.setTransferLimit(BigDecimal.valueOf(1000));

        // Save account into database and get latest to data  back
        account = accountRepository.save(account);

        // Transfer domain Model to DTO
        return accountMapper.toAccountResponse(account);

    }

    @Override
    public Page<AccountResponse> findList(int pageNumber, int pageSize) {

        Sort  sortById = Sort.by(Sort.Direction.DESC, "id");
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sortById);

        Page<Account> accounts = accountRepository.findAll(pageRequest);

        return  accounts.map(accountMapper::toAccountResponse);
    }

    @Override
    public AccountResponse findByActno(String actNo) {

         // Validate account  No
        Account account = accountRepository
                .findByActNo(actNo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Account has not been found "));
        return accountMapper.toAccountResponse(account);
    }


}
