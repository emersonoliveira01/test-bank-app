package com.bank.service;

import com.bank.model.Account;
import com.bank.model.Customer;
import com.bank.enums.CardType;
import com.bank.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CardService cardService;

    @InjectMocks
    private AccountService accountService;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = Customer.builder()
                .id(1L)
                .email("teste@teste.com")
                .name("Teste")
                .address("Rua de teste")
        .build();
    }

    @Test
    void givenCreateAccountForCustomer_ShouldPersistAccountAndCreatePhysicalCard() {

        doNothing().when(accountRepository).persist(any(Account.class));
        doNothing().when(cardService).creteCard(any(Account.class), eq(CardType.PHYSICAL));

        Account account = accountService.createAccountForCustomer(customer);

        verify(accountRepository, times(1)).persist(account);
        verify(cardService, times(1)).creteCard(account, CardType.PHYSICAL);

        assertNotNull(account);
        assertEquals(customer, account.getCustomer());
    }

    @Test
    void givenCreateAccountForCustomer_ShouldThrowException_WhenRepositoryFails() {

        final var expectedMessage = "Erro ao persistir a conta";

        doThrow(new RuntimeException("Erro ao persistir a conta"))
                .when(accountRepository).persist(any(Account.class));

        var exception = assertThrows(RuntimeException.class, () ->
                accountService.createAccountForCustomer(customer));

        assertEquals(expectedMessage, exception.getMessage());

        verify(cardService, never()).creteCard(any(Account.class), any(CardType.class));
    }

}