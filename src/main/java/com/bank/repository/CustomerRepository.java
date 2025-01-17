package com.bank.repository;

import com.bank.model.Customer;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class CustomerRepository implements PanacheRepository<Customer> {

    public Optional<Customer> findByEmail(String email) {
        return find("email", email).firstResultOptional();
    }
}
