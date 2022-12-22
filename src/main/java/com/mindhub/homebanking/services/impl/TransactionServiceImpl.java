package com.mindhub.homebanking.services.impl;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public List<Transaction> findAllTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    public Optional<Transaction> findTransactionById(Long id) {
        return transactionRepository.findById(id);
    }

    @Override
    public List<Transaction> findByDateBetween(LocalDateTime date1, LocalDateTime date2) {
        return transactionRepository.findByDateBetween(date1,date2);
    }

    @Override
    public List<Transaction> findByAmountBetween(Double amount1, Double amount2) {
        return transactionRepository.findByAmountBetween(amount1,amount2);
    }

    @Override
    public List<Transaction> findByType(String type) {
        return transactionRepository.findByType(type);
    }

    @Override
    public void createTransaction(TransactionType type, String description, Double amount, Account account) {
        Double accountBalance = accountService.updateBalance(account, amount);
        Transaction transaction = new Transaction(type, description, LocalDateTime.now(), amount, accountBalance, account);
        transactionRepository.save(transaction);
    }

    @Override
    public void transferAmount(Account fromAccount, Account toAccount, String description, Double amount) {

        String fromAccountNumber = fromAccount.getNumber();

        String toAccountNumber = toAccount.getNumber();

        // GENERO UNA TRANSACCION DE DEBITO EN LA CUENTA ORIGEN Y ACTUALIZO EL SALDO
        Double fromAccountBalance = accountService.updateBalance(fromAccount, -amount);

        Transaction debitTransaction = new Transaction
                (TransactionType.DEBIT
                        ,description + " - " + toAccountNumber
                        , LocalDateTime.now()
                        , -amount
                        , fromAccountBalance
                        , fromAccount);
        transactionRepository.save(debitTransaction);

        // GENERO UNA TRANSACCION DE CREDITO EN LA CUENTA DESTINO Y ACTUALIZO EL SALDO
        Double toAccountBalance = accountService.updateBalance(toAccount, amount);
        Transaction creditTransaction = new Transaction
                (TransactionType.CREDIT
                        ,description + " - " + fromAccountNumber
                        , LocalDateTime.now()
                        , amount
                        , toAccountBalance
                        , toAccount);
        transactionRepository.save(creditTransaction);
    }

    @Override
    public void deleteTransaction(Long id) {
        Transaction transaction = transactionRepository.findById(id).orElse(null);
        transaction.setType(TransactionType.DELETED);
        transactionRepository.save(transaction);
    }
}
