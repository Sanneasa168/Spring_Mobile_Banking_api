package com.example.mobile_banking_api.features.account;

import com.example.mobile_banking_api.features.account.dto.AccountCreateRequest;
import com.example.mobile_banking_api.features.account.dto.AccountRenameRequest;
import com.example.mobile_banking_api.features.account.dto.AccountResponse;
import com.example.mobile_banking_api.features.account.dto.AccountTranasferLimitRequest;
import com.example.mobile_banking_api.features.accountType.AccountTypeService;
import com.example.mobile_banking_api.features.accountType.dto.AccountTypeResponse;
import com.example.mobile_banking_api.features.accountType.dto.AccountTypeUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/accounts")
public class AccountController {
    private final AccountService accountService;
//    private final AccountTypeService accountTypeService;

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{actNo}/limit")
    void updateTransferLimit(@PathVariable("actNo")String actNo, @Valid @RequestBody AccountTranasferLimitRequest accountTranasferLimitRequest) {
        accountService.updateTransferlimit(actNo, accountTranasferLimitRequest);
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{actNo}/hide")
    void hideAccount(@PathVariable("actNo")String actNo) {
        accountService.hindeAccount(actNo);
    }
    @GetMapping("/{actNo}/rename")
    AccountResponse renameAccount(@PathVariable("actNo") String actNo,
                                  @Valid @RequestBody AccountRenameRequest accountRenameRequest) {
        return accountService.renameAccount(actNo,accountRenameRequest);
    }

    @GetMapping
    Page<AccountResponse> findList(
            @RequestParam(required = false,defaultValue = "0") int pageNumber,
            @RequestParam(required = false,defaultValue = "25") int pageSize
    ){
        return  accountService.findList(pageNumber,pageSize);
    }

    AccountResponse findByActNo(@PathVariable("actNo")String actNo){
        return accountService.findByActno(actNo);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping
    AccountResponse createNew(@Valid @RequestBody AccountCreateRequest accountCreateRequest){
        return  accountService.createNew(accountCreateRequest);
    }
}