package com.example.mobile_banking_api.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

@Component
public class JwTConfig {

    @Bean("accessTokenKeyPair")
    KeyPair  accessTokenKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keypairgenerator = KeyPairGenerator.getInstance("RSA");
        keypairgenerator.initialize(2048);
        return keypairgenerator.generateKeyPair();
    }

    @Bean("accessTokenRSAKey")
    RSAKey accessTokenRSAKey(@Qualifier("accessTokenKeyPair") KeyPair keyPair) {
        return new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
                .privateKey((keyPair.getPrivate()))
                .keyID(UUID.randomUUID().toString())
                .build();
    }

    @Bean("accessTokenJwtDecoder")
    JwtDecoder   accessTokenJwtDecoder(@Qualifier("accessTokenRSAKey") RSAKey  rsaKey) throws JOSEException{
        return NimbusJwtDecoder
                .withPublicKey(
                        rsaKey.toRSAPublicKey())
                        .build();
    }

    @Bean("accessTokenJWKSource")
    JWKSource<SecurityContext> accessTokenJWKSource(@Qualifier("accessTokenRSAKey") RSAKey rsaKey){
        JWKSet  jwkSet= new JWKSet(rsaKey);
        return (jwkSelector, securityContext)
                ->  jwkSelector.select(jwkSet);
    }

    @Bean("accessTokenJwtEncoder")
    JwtEncoder  accessTokenJwtEncoder(@Qualifier("accessTokenJWKSource") JWKSource<SecurityContext>jwkSource){
        return new NimbusJwtEncoder(jwkSource);
    }

//    ========================================== Refresh Token ============================================

    @Bean("refreshTokenKeyPair")
    KeyPair refreshTokenKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }

    @Bean("refreshTokenRSAKey")
    RSAKey refreshTokenRSAKey(@Qualifier("refreshTokenKeyPair") KeyPair keyPair) {
        return new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
                .privateKey(keyPair.getPrivate())
                .keyID(UUID.randomUUID().toString())
                .build();
    }

    @Bean("refreshTokenJwtDecoder")
    JwtDecoder refreshTokenJwtDecoder(@Qualifier("refreshTokenRSAKey") RSAKey rsaKey) throws JOSEException {
        return NimbusJwtDecoder
                .withPublicKey(rsaKey.toRSAPublicKey())
                .build();
    }

    @Bean("refreshTokenJWKSource")
    JWKSource<SecurityContext> refreshTokenJWKSource(@Qualifier("refreshTokenRSAKey") RSAKey rsaKey) {
        JWKSet jwkSet = new JWKSet(rsaKey);
        return (jwkSelector, securityContext)
                -> jwkSelector.select(jwkSet);
    }

    @Bean("refreshTokenJwtEncoder")
    JwtEncoder refreshTokenJwtEncoder(@Qualifier("refreshTokenJWKSource") JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }
}

