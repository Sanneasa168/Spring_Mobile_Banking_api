package com.example.mobile_banking_api.features.auth;

import com.example.mobile_banking_api.domain.User;
import com.example.mobile_banking_api.domain.UserVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserVerificationRepository extends JpaRepository<UserVerification, Long> {

    Optional<UserVerification>  findByUserAndVerificationCode(User user,String verifiedCode);

    Optional<UserVerification>  findByUser(User user);

}
