package com.mindhub.homebanking.services.impl;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    AccountRepository accountRepository;
    Random random = new Random();

    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
    }
    @Override
    public Optional<Account> findById(Long id) {
        return accountRepository.findById(id);
    }
    @Override
    public List<Account> findByBalanceGreaterThan(Double balance) {
        return accountRepository.findByBalanceGreaterThan(balance);
    }
    @Override
    public List<Account> findByCreationDateLessThan(LocalDateTime date) {
        return accountRepository.findByCreationDateLessThan(date);
    }
    @Override
    public List<Account> findByOwner(Client client) {
        return accountRepository.findByOwner(client);
    }
    @Override
    public Account findByNumber(String number) {
        return accountRepository.findByNumber(number);
    }
    @Override
    public Account createAccount(Client client, AccountType accountType) {
        String vinNumber = AccountUtils.generateVinNumber();
        return accountRepository.save(new Account(vinNumber, LocalDateTime.now(), (double) 0, accountType, client));
    }
    @Override
    public Double updateBalance(Account account, Double amount) {
        Double accountBalance = account.getBalance() + amount;
        account.setBalance(accountBalance);
        accountRepository.save(account);
        return accountBalance;
    }
    @Override
    public Account findAccountByClientAndNumber (Client client,String number) {
        return accountRepository.findByOwnerAndNumber(client,number);
    }
    @Override
    public void deleteAccount (Long id) {
        Account account = accountRepository.findById(id).orElse(null);
        account.setActive(false);
        accountRepository.save(account);
    }

}
