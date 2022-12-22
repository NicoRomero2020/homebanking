package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionService {

    List <Transaction> findAllTransactions();
    Optional<Transaction> findTransactionById(Long id);
    List<Transaction> findByDateBetween (LocalDateTime date1, LocalDateTime date2);
    List<Transaction> findByAmountBetween (Double amount1, Double amount2);
    List<Transaction> findByType (String type);
    void createTransaction (TransactionType type, String description, Double amount, Account account);
    void deleteTransaction (Long id);
    void transferAmount (Account fromAccount, Account toAccount, String description, Double amount);
}
