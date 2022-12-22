package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientLoanDTO;
import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.repositories.ClientLoanRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class ClientLoanController {
    @Autowired
    ClientLoanRepository clientLoanRepository;
    @Autowired
    LoanService loanService;
    @Autowired
    ClientService clientService;
    @Autowired
    AccountService accountService;

    @GetMapping("/clientLoans")
    public List<ClientLoanDTO> getClientLoans(){
        return clientLoanRepository.findAll().stream().map(ClientLoanDTO::new).collect(toList());
    }

    @GetMapping("/clientLoans/{id}")
    public ClientLoanDTO getClientLoan(@PathVariable long id){
        return clientLoanRepository.findById(id).map(ClientLoanDTO::new).orElse(null);
    }

    @GetMapping("/clientLoans/client/{clientId}")
    public List<ClientLoanDTO> getClientLoansByClient(@PathVariable Long clientId){
        Client client = clientService.findClientById(clientId).orElse(null);
        return clientLoanRepository.findByClient(client).stream().map(ClientLoanDTO::new).collect(toList());
    }

    //TODO
    //ESTE METODO DEBE BUSCAR LOS PRESTAMOS DE CLIENTES QUE TENGAN UN BALANCE MAYOR AL INFORMADO
    /*
    @GetMapping("/clientLoans/client/{client}/balance/{balance}")
    public List<ClientLoanDTO> getClientLoansByBalance(@PathVariable Long clientId, @PathVariable Double balance){
    }*/

    @GetMapping("/clientLoans/amount/{amount}")
    public List<ClientLoanDTO> getClientLoansByBalance(@PathVariable Double amount){
        return clientLoanRepository.findByAmountGreaterThan(amount).stream().map(ClientLoanDTO::new).collect(toList());
    }

    @Transactional
    @PostMapping(path = "/clients/current/loans")
    public ResponseEntity<Object> createClientLoan(@RequestBody LoanApplicationDTO newClientLoan, Authentication authentication) {

        if (       newClientLoan.getLoanId() == null
                || newClientLoan.getAmount() == 0
                || newClientLoan.getPayments() == 0
                || newClientLoan.getToAccountNumber().isEmpty() ) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        Client loggedClient = clientService.findByEmail(authentication.getName());
        Account account = accountService.findByNumber(newClientLoan.getToAccountNumber());
        Loan loan = loanService.findLoanById(newClientLoan.getLoanId());
        if (loan == null) {
            return new ResponseEntity<>("Loan does not exists", HttpStatus.FORBIDDEN);
        }
        if (newClientLoan.getAmount() > loan.getMaxAmount()) {
            return new ResponseEntity<>("Requested amount is greater than the allowed limit", HttpStatus.FORBIDDEN);
        }
        if (!loan.getPayments().contains(newClientLoan.getPayments())) {
            return new ResponseEntity<>("Invalid payments quantity", HttpStatus.FORBIDDEN);
        }
        if (account == null) {
            return new ResponseEntity<>("Account does not exists", HttpStatus.FORBIDDEN);
        }
        if (!account.getOwner().equals(loggedClient)) {
            return new ResponseEntity<>("The account does not belong to the client", HttpStatus.FORBIDDEN);
        }
        loanService.createClientLoan(loggedClient, account, loan, newClientLoan.getAmount(), newClientLoan.getPayments());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
