package com.example.mobile_banking_api.features.auth;

import com.example.mobile_banking_api.domain.Role;
import com.example.mobile_banking_api.domain.User;
import com.example.mobile_banking_api.domain.UserVerification;
import com.example.mobile_banking_api.features.auth.dto.*;
import com.example.mobile_banking_api.features.user.RoleRepository;
import com.example.mobile_banking_api.features.user.UserRepository;
import com.example.mobile_banking_api.mapper.UserMapper;
import com.example.mobile_banking_api.util.RandomUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;
    private final UserVerificationRepository userVerificationRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    private final DaoAuthenticationProvider daoAuthenticationProvider;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    private final JwtEncoder accessTokenJwtEncoder;
    private final JwtEncoder refreshTokenJwtEncoder;

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String adminEmail;

    @Override
    public AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest){

        String refreshToken = refreshTokenRequest.refreshToken();

        // Authenticate client with refresh Token
        Authentication auth = new BearerTokenAuthenticationToken(refreshToken);
        auth = jwtAuthenticationProvider.authenticate(auth);

        log.info("Auth :{} ",auth.getPrincipal());

        Jwt jwt = (Jwt) auth.getPrincipal();

        Instant now = Instant.now();
        JwtClaimsSet  jwtAccessClaimsSet = JwtClaimsSet.builder()
                .id(jwt.getId())
                .subject("Access APIs")
                .issuer(jwt.getId())
                .issuedAt(now)
                .expiresAt(now.plus(10,ChronoUnit.SECONDS))
                .audience((jwt.getAudience()))
                .claim("isAdmin",true)
                .claim("StudentId","ISTAD00010")
                .claim("scope",jwt.getClaimAsString("scope"))
                .build();

        refreshToken = refreshTokenJwtEncoder
                .encode(JwtEncoderParameters.from( jwtAccessClaimsSet))
                .getTokenValue();

        // Get expiration of refresh token
        Instant expiresAt = jwt.getExpiresAt();
        long  remainingDays = Duration.between(now, expiresAt).toDays();
        log.info("remaining Days:{}",remainingDays);
        if(remainingDays <= 1){
            JwtClaimsSet jwtRefreshClaimsSet =  JwtClaimsSet.builder()
                    .id(auth.getName())
                    .subject("Refresh Token")
                    .issuer(auth.getName())
                    .issuedAt(now)
                    .expiresAt(now.plus(7,ChronoUnit.DAYS))
                    .audience(List.of("NextJs","Flutter","React Native"))
                    .claim("scope",jwt.getClaimAsString("scope"))
                    .build();
            refreshToken = refreshTokenJwtEncoder
                    .encode(JwtEncoderParameters.from(jwtRefreshClaimsSet))
                    .getTokenValue();
        }
        return AuthResponse.builder()
                .tokenType("Bearer")
                .accessToken(refreshToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest loginRequest) {

        log.info("Access Token :{}",accessTokenJwtEncoder);
        log.info("Refresh Token :{}",refreshTokenJwtEncoder);

        // Authenticate client  with username(PhoneNumber) and password

        Authentication auth = new  UsernamePasswordAuthenticationToken(loginRequest.phoneNumber(),loginRequest.password());
        auth = daoAuthenticationProvider.authenticate(auth);

        log.info("Auth:{}", auth.getPrincipal() );

        //ROLE_USER_ADMIN
        String scope = auth
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
            log.info("scope:{}",scope);

        //Generate JWT token by  jwtEncoder
        // 1. define JwtClaimsSet (Payload)
        Instant now = Instant.now();
        JwtClaimsSet jwtAccessClaimsSet = JwtClaimsSet.builder()
                .id(auth.getName())
                .subject("Access APIs ")
                .issuer(auth.getName())
                .issuedAt(now)
                .expiresAt(now.plus(10, ChronoUnit.SECONDS))
                .audience(List.of("NextJs","Android","IOS"))
                .claim("isAdmin",true)
                .claim("StudentId","ISTAD001")
                .claim("scope",scope)
                .build();



        // 1. define JwtClaimsSet (Payload)
        JwtClaimsSet  jwtRefreshClaimsSet = JwtClaimsSet.builder()
                .id(auth.getName())
                .subject(" Refresh Token APIs ")
                .issuer(auth.getName())
                .issuedAt(now)
                .expiresAt(now.plus(1,ChronoUnit.DAYS))
                .audience(List.of("NextJs","Android","IOS"))
                .claim("scope",scope)
                .build();



        //2. Generate Token
        String accessToken  =  accessTokenJwtEncoder
                .encode(JwtEncoderParameters.from(jwtAccessClaimsSet))
                .getTokenValue();

        //2 Generate Token
        String refreshToken  =  refreshTokenJwtEncoder
                .encode(JwtEncoderParameters.from(jwtRefreshClaimsSet))
                .getTokenValue();

        log.info("Access Token: {}",accessToken);
        log.info("refresh Token: {}",refreshToken);
        return
                AuthResponse.builder()
                        .accessToken("Bearer")
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
    }

    @Override
    public void verify(VerificationRequest verificationRequest) {

        // Validate Email
        User user = userRepository
                .findByEmail(verificationRequest.email())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User has not been found "
                ));

        // Validate  verified Code
        UserVerification userVerification =   userVerificationRepository
                .findByUserAndVerificationCode(user,verificationRequest.verifiedCode())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User verification has not been found "
                ));

        // Is verified code expired
        if(LocalTime.now().isAfter(userVerification.getExpiryTime())){
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Verified code has expired"
            );
        }
        user.setIsVerified(true);
        userRepository.save(user);

        userVerificationRepository.delete(userVerification);

    }

    @Override
    public void sendVerification(String email) throws MessagingException {
     // Validate email
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User has not been found "
                ));

            UserVerification userVerification = new UserVerification();
            userVerification.setUser(user);
            userVerification.setVerificationCode(RandomUtil.random6Digits());
            userVerification.setExpiryTime(LocalTime.now().plusMinutes(1));
           userVerificationRepository.save(userVerification);

        // Prepare email for  sending
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setTo(email);
        helper.setFrom(adminEmail);
        helper.setSubject("User Verification ");
        helper.setText(userVerification.getVerificationCode());

        javaMailSender.send(message);
    }

    @Override
    public void resendVerification(String email) throws MessagingException {
        // Validate email
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User has not been found "
                ));

        UserVerification userVerification = userVerificationRepository
                .findByUser(user)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User has not been found "
                ));
        userVerification.setVerificationCode(RandomUtil.random6Digits());
        userVerification.setExpiryTime(LocalTime.now().plusMinutes(1));
        userVerificationRepository.save(userVerification);

        // Prepare email for  sending
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setTo(email);
        helper.setFrom(adminEmail);
        helper.setText(userVerification.getVerificationCode());

        javaMailSender.send(message);
    }



    @Override
    public RegisterResponse register(RegisterRequest registerRequest) {


        // Validate user's phoneNumber
        if (userRepository.existsByEmail(registerRequest.phoneNumber())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Phone Number  has already been used");
        }

        // Validate user email
        if(userRepository.existsByEmail(registerRequest.email())){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Email already been used "
            );
        }

        // Validate password
        if(!registerRequest.password().equals(registerRequest.confirmedPassword())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST ,
                    " Password does not match ");
        }

        // Validate national ID card
        if (userRepository.existsByNationalCardId(registerRequest.nationalCardId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "National card ID has already been used");
        }
        // Validation term and policy
        if (!registerRequest.acceptTerm()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "You must accept the term");
        }

        User user = userMapper.formRegisterRequest(registerRequest);

        //Set System data
        user.setUuid(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setProFileImage("profile/default-user.png");
        user.setIsBlocked(false);
        user.setIsDeleted(false);
        user.setIsVerified(false);

        Role roleUser = roleRepository.findRoleUser(); // default role
        Role roleCustomer = roleRepository.findRoleCustomer();
        List<Role> roles = List.of(roleUser, roleCustomer);
        user.setRoles(roles);
        userRepository.save(user);

        return RegisterResponse.builder()
                .message("You have registered successfully, please verify an email!")
                .email(user.getEmail())
                .build();
    }
}
