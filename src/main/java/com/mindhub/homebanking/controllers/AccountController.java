package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    AccountService accountService;
    @Autowired
    ClientService clientService;
    @Autowired
    TransactionService transactionService;

    @GetMapping("/accounts")
    public List<AccountDTO> getAccounts(){
        return accountService.findAll().stream().map(AccountDTO::new).collect(toList());
    }

    @GetMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable long id){
        return accountService.findById(id).map(AccountDTO::new).orElse(null);
    }

    @GetMapping("/accounts/number/{number}")
    public AccountDTO getAccountByNumber(@PathVariable String number){
       return new AccountDTO(accountService.findByNumber(number));
    }

    @GetMapping("/accounts/balance/{amount}")
    public List<AccountDTO> getAccountsByBalance(@PathVariable Double amount){
        return accountService.findByBalanceGreaterThan(amount).stream().map(AccountDTO::new).collect(toList());
    }

    @GetMapping("/accounts/date/{date}")
    public List<AccountDTO> getAccountsByBalance(@PathVariable LocalDateTime date){
        return accountService.findByCreationDateLessThan(date).stream().map(AccountDTO::new).collect(toList());
    }

    @GetMapping("/clients/current/accounts")
    public List<AccountDTO> getAccountsByLoggedClient(Authentication authentication) {
        Client loggedClient = clientService.findByEmail(authentication.getName());
        return accountService.findByOwner(loggedClient).stream().map(AccountDTO::new).collect(toList());
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createAccount(Authentication authentication
                                                ,@RequestParam AccountType accountType) {
        Client loggedClient = clientService.findByEmail(authentication.getName());
        if (loggedClient.getAccounts().stream().filter((account -> accountType.equals(account.getAccountType()))).count() >= 3) {
            return new ResponseEntity<>("Maximum account limit exceeded", HttpStatus.FORBIDDEN);
        }
        accountService.createAccount(loggedClient, accountType);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Transactional
    @DeleteMapping("/clients/current/accounts")
    public ResponseEntity<Object> deleteAccount(Authentication authentication
                                               ,@RequestParam String accountNumber) {
        Client loggedClient = clientService.findByEmail(authentication.getName());
        Account account = accountService.findAccountByClientAndNumber(loggedClient, accountNumber);
        List<Long> transactionId = account.getTransactions().stream().map(Transaction::getId).collect(toList());
        for (Long id: transactionId) {
            transactionService.deleteTransaction(id);
        }
        accountService.deleteAccount(account.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
