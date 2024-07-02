package com.example.mobile_banking_api.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name="transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Account owner;

    @ManyToOne
    private Account receiver;

    private String paymentReceiver;   // StudentCardId ,BillNo ,PhoneNumber

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable =false ,length=30)
    private String transactionType; // Payment, Transfer

    @Column(nullable =false)
    private LocalDateTime transactionAt;

    @Column(nullable =false)
    private Boolean Status;

    @Column(nullable =false)
    private Boolean isDeleted;
}
