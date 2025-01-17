package com.bank.service;

import com.bank.controller.request.CustomerRequest;
import com.bank.model.Account;
import com.bank.model.Customer;
import com.bank.enums.AccountStatus;
import com.bank.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private CustomerService customerService;

    private CustomerRequest customerRequest;
    private Customer customer;
    private Account account;

    @BeforeEach
    void setup() {
        customerRequest = CustomerRequest
                .builder()
                .name("Teste")
                .email("teste@example.com")
                .address("Rua teste")
        .build();

        customer = Customer.with(customerRequest);

        account = Account.builder()
                .id(1L)
                .status(AccountStatus.ACTIVE)
       .build();
    }

    @Test
    void givenCreateCustomer_whenCallCustomers_thenSuccess() {

        when(customerRepository.findByEmail(customerRequest.getEmail())).thenReturn(Optional.empty());
        when(accountService.createAccountForCustomer(any())).thenReturn(account);

        customerService.createCustomer(customerRequest);

        verify(customerRepository, times(2)).persist(any(Customer.class));
        verify(accountService, times(1)).createAccountForCustomer(any(Customer.class));
    }

    @Test
    void givenCreateCustomer_whenCallCustomers_thenEmailAlreadyExists() {

        final var expectedMessage = "Já existe um cadastro com email informado.";

        when(customerRepository.findByEmail(customerRequest.getEmail())).thenReturn(Optional.of(customer));

        var exception = assertThrows(IllegalArgumentException.class, () ->
                customerService.createCustomer(customerRequest));

        assertEquals(expectedMessage, exception.getMessage());
        verify(customerRepository, never()).persist(any(Customer.class));
    }

    @Test
    void givenCancelAccount_whenCallCustomers_thenCancelAccount_Success() {

        customer.setAccount(account);
        when(customerRepository.findById(1L)).thenReturn(customer);

        customerService.cancelAccount(1L);

        assertEquals(AccountStatus.INACTIVE, customer.getAccount().getStatus());

        verify(customerRepository, times(1)).persist(customer);
    }

    @Test
    void givenCancelAccount_whenCallCustomers_thenCustomerNotFound() {

        final var expectedMessage = "Cadastro não encontrado.";

        when(customerRepository.findById(1L)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                customerService.cancelAccount(1L));

        assertEquals(expectedMessage, exception.getMessage());
        verify(customerRepository, never()).persist(any(Customer.class));
    }

}