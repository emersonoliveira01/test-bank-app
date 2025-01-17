package com.bank.model;

import com.bank.enums.CardStatus;
import com.bank.enums.CardType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String cardNumber;

    @Enumerated(EnumType.STRING)
    private CardType cardType;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column
    private String cvv;

    @Column
    private LocalDateTime expirationDate;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime deliveryDate;

    @Column
    private String deliveryReturnReason;

    @Column
    private String deliveryAddress;

    @Column
    private String tracking;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CardStatus status;

    public static Card with(Account account, CardType cardType) {
        return Card.builder()
                .account(account)
                .cardType(cardType)
                .expirationDate(generateExpirationDate())
                .cvv(generateCVV())
                .cardNumber(generateCardNumber())
                .createdAt(LocalDateTime.now())
                .status(CardStatus.CREATE)
                .tracking(UUID.randomUUID().toString())
                .build();
    }

    private static String generateCardNumber() {
        return "CARD-" + (1000 + (int)(Math.random() * 9000)) + "-" + System.currentTimeMillis();
    }

    private static String generateCVV() {
        return String.valueOf(100 + (int)(Math.random() * 900));
    }

    private static LocalDateTime generateExpirationDate() {
        return LocalDateTime.now().plusYears(5);
    }
}
