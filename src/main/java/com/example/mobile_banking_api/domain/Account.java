package com.example.mobile_banking_api.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "accounts"
)
    public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String  alias;
    private String actNo;
    private String actName;
    private BigDecimal balance;
    private BigDecimal transferLimit;
    private Boolean isHidden;
    private Boolean isDeleted; // soft delete

    @ManyToOne
    private AccountType accountType;

    @OneToOne
    private Card card;

    @ManyToOne
    @JoinTable(
            name ="user_accounts",
            joinColumns =  @JoinColumn(name="account_id",referencedColumnName ="id"),
            inverseJoinColumns =  @JoinColumn(name="user_id",referencedColumnName = "id")
    )
    private User user;

    @OneToMany(mappedBy = "owner")
    private List<Transaction> transactionOfOwner;

}
