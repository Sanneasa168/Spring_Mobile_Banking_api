package com.example.mobile_banking_api.features.user;

import com.example.mobile_banking_api.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository  extends JpaRepository<User,Integer> {

    //SELECT EXIST (SELECT *FROM WHERE phone_number
    Boolean existsByPhoneNumber(String phoneNumber);

    Boolean existsByEmail(String email);

    Boolean existsByNationalCardId(String nationalCardId);

//    SELECT *  FROM  USERS WHERE UUID = ?
    Optional<User> findByPhoneNumberAndIsDeletedFalse(String phoneNumber);

    // SELECT * FROM user where   uuid = ?
    Optional<User> findByUuid(String uuid);

    Optional<User> findByEmail(String email);

}
