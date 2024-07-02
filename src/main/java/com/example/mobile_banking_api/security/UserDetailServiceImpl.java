package com.example.mobile_banking_api.security;

import com.example.mobile_banking_api.domain.User;
import com.example.mobile_banking_api.features.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        User user =  userRepository
                .findByPhoneNumberAndIsDeletedFalse(phoneNumber)
                .orElseThrow(() -> new UsernameNotFoundException("User has not been found"));

        log.info("User: {}", user.getPhoneNumber());
        CustomUserDetails customUserDetails = new CustomUserDetails();
        customUserDetails.setUser(user);
        return customUserDetails;
    }
}
