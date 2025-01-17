package com.bank.model;

import com.bank.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String accountNumber;

    @OneToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<Card> cards;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    public static Account with(Customer customer) {
        return Account.builder()
                .customer(customer)
                .accountNumber(generateAccountNumber())
                .createdAt(LocalDateTime.now())
                .status(AccountStatus.ACTIVE)
                .build();

    }

    private static String generateAccountNumber() {
        return "ALT-" + System.currentTimeMillis();
    }
}
