package com.bank.model;

import com.bank.controller.request.CustomerRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String address;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    private Account account;

    public static Customer with(CustomerRequest customerRequest) {
        return Customer.builder()
                .email(customerRequest.getEmail())
                .name(customerRequest.getName())
                .address(customerRequest.getAddress())
                .build();
    }
}
