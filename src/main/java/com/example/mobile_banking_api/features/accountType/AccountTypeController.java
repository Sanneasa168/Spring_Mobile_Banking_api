package com.example.mobile_banking_api.features.accountType;
import com.example.mobile_banking_api.features.accountType.dto.AccountTypeRequest;
import com.example.mobile_banking_api.features.accountType.dto.AccountTypeResponse;
import com.example.mobile_banking_api.features.accountType.dto.AccountTypeUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/account_type")
@RequiredArgsConstructor
public class AccountTypeController {

    private final AccountTypeService  accountTypeService;

    @GetMapping("/alias")
    AccountTypeResponse findByAlias(@PathVariable("alias") String alias){
        return accountTypeService.findByAlias(alias);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{alias}")
    void deleteByAlias(@PathVariable("alias")  String alias){
        accountTypeService.deleteByAlias(alias);
    }

    @PatchMapping("/{alias}")
    AccountTypeResponse updateByAlias(@PathVariable  String alias,
                                      @RequestBody AccountTypeUpdateRequest accountTypeUpdateRequest){
        return accountTypeService.updateByAlias(alias,accountTypeUpdateRequest);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    void createNew(@Valid @RequestBody  AccountTypeRequest accountTypeRequest){
        accountTypeService.createNew(accountTypeRequest);
    }

    @GetMapping
    List<AccountTypeResponse>  findList(){
        return accountTypeService.findList();
    }

}
