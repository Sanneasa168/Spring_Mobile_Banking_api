package com.example.mobile_banking_api.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity

@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique=true)
    private String uuid;

    @Column(nullable = false,unique = true,length = 50)
    private String email;

    @Column(nullable = false,length=10, unique = true)
    private String phoneNumber;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false,length = 4)
    private String pin;

    @Column(nullable = false,length =20, unique =  true)
    private String nationalCardId;

    @Column(nullable = false,length=100)
    private String name;

    @Column(length =10, nullable = false)
    private String gender;

    private String  proFileImage;


    @Column(length=20, unique = true)
    private String studentCardId;

    private String  street;
    private String village;
    private String sangkatOrCommune;
    private String khanOrDistrict;
    private String cityOrProvince;

    private String position;
    private BigDecimal monthlyIncomeRange;
    private String employeeType;
    private String companyName;
    private String mainSourceOfIncome;

    private Boolean isDeleted;
    private Boolean isBlocked;
    private Boolean isVerified;

    @OneToMany(mappedBy = "user")
    private List<Account> accounts;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="users_roles",
            joinColumns = @JoinColumn(name = "user_id",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id",referencedColumnName = "id"))
    private List<Role> roles;

}
