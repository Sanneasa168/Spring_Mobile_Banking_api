package com.example.mobile_banking_api.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.DaoAuthenticationConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

  /*  @Bean
    InMemoryUserDetailsManager configuredUserSecurity(){
        UserDetails  admin = User
                .withUsername("admin")
                .password(passwordEncoder.encode("admin123"))
                .roles("USER","ADMIN")
                .build();
        UserDetails  editor = User
                .withUsername("editor")
                .password(passwordEncoder.encode("editor123"))
                .roles("USER","EDITOR")
                .build();
        InMemoryUserDetailsManager  manager = new InMemoryUserDetailsManager();
        manager.createUser(admin);
        manager.createUser(editor);
        return  manager;
    }
    */
    @Bean
  JwtAuthenticationProvider configJwtAuthenticationProvider(@Qualifier("refreshTokenJwtDecoder") JwtDecoder refreshTokenJwtDecoder) {
        JwtAuthenticationProvider auth = new JwtAuthenticationProvider(refreshTokenJwtDecoder);
        return auth;
    }

    @Bean
    DaoAuthenticationProvider configDaoAuthenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userDetailsService);
        auth.setPasswordEncoder(passwordEncoder);
        return auth;

    }
    @Bean
    SecurityFilterChain confiqureApiSecurity(HttpSecurity http,
                                             @Qualifier("accessTokenJwtDecoder") JwtDecoder jwtDecode)throws Exception{

        // Endpoint security config
        http.authorizeHttpRequests((endpoint->endpoint
                .requestMatchers("/api/v1/auth/**",
                        "/api/v1/upload/**",
                        "/upload/**")
                .permitAll()
                .requestMatchers(HttpMethod.POST,"/aip/v1/account_type/**").hasAnyAuthority("SCOPE_MANAGER","SCOPE_ADMIN")
                .requestMatchers(HttpMethod.DELETE,"/aip/v1/accounts_type/**").hasAnyAuthority("SCOPE_ADMIN")
                .requestMatchers(HttpMethod.GET,"/aip/v1/accounts_type/**").hasAnyAuthority("SCOPE_MANAGER","SCOPE_ADMIN")
                .requestMatchers(HttpMethod.PUT,"/aip/v1/accounts_type/**").hasAnyAuthority("SCOPE_MANAGER","SCOPE_ADMIN")
                .requestMatchers(HttpMethod.PATCH,"/aip/v1/accounts_type/**").hasAnyAuthority("SCOPE_MANAGER","SCOPE_ADMIN")
                .anyRequest().authenticated()
        ));

        // Security Mechanism (HTTP Basic Auth)
        // HTTP Basic Auth (Username & Password)
//        http.httpBasic(Customizer.withDefaults());

        //Security Mechanism (JWT)
        http.oauth2ResourceServer(jwt-> jwt
                        .jwt(jwtConfigurer -> jwtConfigurer
                                .decoder(jwtDecode))
                );

        // Disable CSRF ( Cross Sit Request Forgery )  Token
        http.csrf(AbstractHttpConfigurer::disable);

        // Make Stateless Session
        http.sessionManagement(
                session-> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

}
