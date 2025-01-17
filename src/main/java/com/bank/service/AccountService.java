package com.bank.service;

import com.bank.model.Account;
import com.bank.model.Customer;
import com.bank.enums.CardType;
import com.bank.repository.AccountRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AccountService {

    @Inject
    AccountRepository accountRepository;

    @Inject
    CardService cardService;

    public Account createAccountForCustomer(Customer customer) {
        var account =  Account.with(customer);

        accountRepository.persist(account);
        createPhysicalCard(account);
        return account;
    }

    private void createPhysicalCard(Account account) {
        cardService.creteCard(account, CardType.PHYSICAL);

    }
}
