package com.bank.controller;

import com.bank.controller.request.CustomerRequest;
import com.bank.service.CustomerService;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    private CustomerRequest customerRequest;

    @BeforeEach
    void setup() {
        customerRequest = CustomerRequest
                .builder()
                .name("Teste")
                .email("teste@example.com")
                .address("Rua teste")
        .build();
    }

    @Test
    void givenCreateCustomer_whenCallCustomers_thenSuccess() {

        final var expectedMessage = "Cadastro criado com sucesso.";

        doNothing().when(customerService).createCustomer(customerRequest);

        var response = customerController.createCustomer(customerRequest);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertEquals(expectedMessage, response.getEntity());

        verify(customerService, times(1)).createCustomer(customerRequest);
    }

    @Test
    void givenCreateCustomer_whenCallCustomers_thenEmailAlreadyExists() {

        final var expectedMessage = "Já existe um cadastro com email informado.";

        doThrow(new IllegalArgumentException("Já existe um cadastro com email informado."))
                .when(customerService).createCustomer(customerRequest);

        var response = customerController.createCustomer(customerRequest);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals(expectedMessage, response.getEntity());

        verify(customerService, times(1)).createCustomer(customerRequest);
    }

    @Test
    void givenCancelAccount_whenCallCustomers_thenCancelAccount_Success() {

        doNothing().when(customerService).cancelAccount(1L);

        var response = customerController.cancelAccount(1L);

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
        assertNull(response.getEntity());

        verify(customerService, times(1)).cancelAccount(1L);
    }

    @Test
    void givenCancelAccount_whenCallCustomers_thenCustomerNotFound() {

        final var expectedMessage = "Cadastro não encontrado.";

        doThrow(new IllegalArgumentException("Cadastro não encontrado."))
                .when(customerService).cancelAccount(1L);

        var response = customerController.cancelAccount(1L);

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertEquals(expectedMessage, response.getEntity());

        verify(customerService, times(1)).cancelAccount(1L);
    }

}