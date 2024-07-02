package com.example.mobile_banking_api.domain;

import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.logging.Log;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="user_verifications")
public class UserVerification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String verificationCode;

    private LocalTime expiryTime;

    @OneToOne
    private User user;
}
