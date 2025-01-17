package com.bank.service;

import com.bank.controller.request.CustomerRequest;
import com.bank.model.Customer;
import com.bank.enums.AccountStatus;
import com.bank.repository.CustomerRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.Objects;

@ApplicationScoped
public class CustomerService {

    @Inject
    CustomerRepository customerRepository;

    @Inject
    AccountService accountService;

    @Transactional
    public void createCustomer(CustomerRequest customerRequest) {

        var byEmail = customerRepository.findByEmail(customerRequest.getEmail());

        if (byEmail.isPresent()) {
            throw new IllegalArgumentException("Já existe um cadastro com email informado.");
        }

        var customer = Customer.with(customerRequest);
        customerRepository.persist(customer);

        createAccount(customer);
    }

    private void createAccount(Customer customer) {
        var account = accountService.createAccountForCustomer(customer);
        customer.setAccount(account);

        customerRepository.persist(customer);
    }

    public void cancelAccount(Long customerId) {
        var customer = customerRepository.findById(customerId);

        if (Objects.isNull(customer)) {
            throw new IllegalArgumentException("Cadastro não encontrado.");
        }

        customer.getAccount().setStatus(AccountStatus.INACTIVE);
        customerRepository.persist(customer);
    }
}
