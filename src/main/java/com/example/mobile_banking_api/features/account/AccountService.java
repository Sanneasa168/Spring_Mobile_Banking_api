package com.example.mobile_banking_api.features.account;

import com.example.mobile_banking_api.features.account.dto.*;
import org.springframework.data.domain.Page;

import java.util.List;


public interface AccountService {

//     void daleteAccount(String actNo);

     void isDeleted(String actNo);
//

     void updateTransferlimit(String actNo, AccountTranasferLimitRequest  accountTranasferLimitRequest);

     void  hindeAccount(String actNo);

     AccountResponse renameAccount(String actNo,AccountRenameRequest accountRenameRequest);
     /**
      * Create a new account
      * @param accountCreateRequest {@link AccountCreateRequest}
      * @return {@link AccountResponse}
      */

     AccountResponse createNew(AccountCreateRequest accountCreateRequest);

     /**
      * Find account by account no
      * @param actno
      * * @return {@link List<AccountResponse>}
      */
     AccountResponse findByActno(String actno);

     /**
      * find all accounts by pagination
      * @param pageNumber is current page request from client
      * @param pageSize is a size of record per to page  from client
      * @return {@link List<AccountRepository>}
      */
     Page<AccountResponse> findList(int pageNumber, int pageSize);



}
