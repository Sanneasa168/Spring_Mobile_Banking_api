package com.example.mobile_banking_api.features.account;

import com.example.mobile_banking_api.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

    Optional<Account> findByActNo(String actNo);


//     Selected  exists( SELECT * FROM accounts WHERE act_no = ?)
    Boolean existsByactNo(String actNo);

}
