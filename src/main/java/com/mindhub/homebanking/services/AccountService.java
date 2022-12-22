package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AccountService {
    List<Account> findAll();
    Optional<Account> findById (Long id);
    List<Account> findByBalanceGreaterThan (Double balance);
    List<Account> findByCreationDateLessThan (LocalDateTime date);
    List<Account> findByOwner (Client client);
    Account findByNumber (String number);
    Account createAccount (Client client, AccountType accountType);
    Double updateBalance (Account account, Double amount);
    Account findAccountByClientAndNumber (Client client, String number);
    void deleteAccount (Long id);
}
