package com.example.mobile_banking_api.features.auth;

import com.example.mobile_banking_api.features.auth.dto.*;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {


    AuthResponse login(LoginRequest loginRequest);

    AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest);

    void verify(VerificationRequest verificationRequest);

    void sendVerification(String email) throws MessagingException;

    void resendVerification(String email) throws MessagingException;

    RegisterResponse register(RegisterRequest registerRequest);
}
