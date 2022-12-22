package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
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
public class TransactionController {
    @Autowired
    private ClientService clientService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private AccountService accountService;

    @RequestMapping("/transactions")
    public List<TransactionDTO> getTransactions(){
        return transactionService.findAllTransactions().stream().map(TransactionDTO::new).collect(toList());
    }

    @RequestMapping("/transactions/{id}")
    public TransactionDTO getTransaction(@PathVariable long id){
        return transactionService.findTransactionById(id).map(TransactionDTO::new).orElse(null);
    }

    @RequestMapping("/transactions/date/{date1}/{date2}")
    public List<TransactionDTO> getTransactionsByDate(@PathVariable LocalDateTime date1,@PathVariable LocalDateTime date2){
        return transactionService.findByDateBetween(date1, date2).stream().map(TransactionDTO::new).collect(toList());
    }

    @RequestMapping("/transactions/balance/{amount}")
    public List<TransactionDTO> getTransactionsByBalance(@PathVariable Double amount1, @PathVariable Double amount2){
        return transactionService.findByAmountBetween(amount1, amount2).stream().map(TransactionDTO::new).collect(toList());
    }

    @RequestMapping("/transactions/type/{type}")
    public List<TransactionDTO> getTransactionsByType(@PathVariable String type){
        return transactionService.findByType(type).stream().map(TransactionDTO::new).collect(toList());
    }

    // ES UNA PETICION TRANSACCIONAL, ANTE UN ERROR SE HACE ROLLBACK DE TODAS LAS OPERACIONES
    @Transactional

    // PETICION PARA REALIZAR TRANSFERENCIAS DE FONDOS ENTRE CUENTAS
    @RequestMapping(path = "/transactions", method = RequestMethod.POST)
    public ResponseEntity<Object> createTransfer(
              @RequestParam String fromAccountNumber
            , @RequestParam String toAccountNumber
            , @RequestParam Double amount
            , @RequestParam String description
            , Authentication authentication) {

        // VALIDO QUE TODOS LOS PARAMETROS HAYAN SIDO INGRESADOS POR LA WEB
        if (amount == 0 || description.isEmpty() || fromAccountNumber.isEmpty() || toAccountNumber.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        // CUENTA ORIGEN Y DESTINO NO PUEDEN SER IGUALES
        if (fromAccountNumber.equals(toAccountNumber)) {
            return new ResponseEntity<>("Invalid transaction", HttpStatus.FORBIDDEN);
        }

        // RECUPERO EL CLIENTE CON LA SESION INICIADA
        Client loggedClient = clientService.findByEmail(authentication.getName());

        // A PARTIR DEL NUMERO DE CUENTA ORIGEN INGRESADO POR LA WEB, RECUPERO LA CUENTA
        Account selectedAccount = accountService.findByNumber(fromAccountNumber);

        // SI LA CUENTA NO EXISTE, MUESTRO ERROR
        if (selectedAccount == null) {
            return new ResponseEntity<>("Origin account does not exist", HttpStatus.FORBIDDEN);
        }

        // LA CUENTA DEBE PERTENECER AL CLIENTE LOGGEADO
        if (!selectedAccount.getOwner().equals(loggedClient)) {
            return new ResponseEntity<>("The account does not belong to the client", HttpStatus.FORBIDDEN);
        }

        // LA CUENTA DEBE TENER SALDO SUFICIENTE PARA LA OPERACION
        if (selectedAccount.getBalance() < amount) {
            return new ResponseEntity<>("Insufficient balance", HttpStatus.FORBIDDEN);
        }

        // A PARTIR DEL NUMERO DE CUENTA DESTINO INGRESADO POR LA WEB, RECUPERO LA CUENTA
        Account destAccount = accountService.findByNumber(toAccountNumber);

        // SI LA CUENTA NO EXISTE, MUESTRO ERROR
        if (destAccount == null) {
            return new ResponseEntity<>("Destination account does not exist", HttpStatus.FORBIDDEN);
        }

        // GENERO UNA TRANSACCION DE DEBITO EN LA CUENTA ORIGEN Y UNA TRANSACCION DE CREDITO EN LA CUENTA DESTINO
        // ACTUALIZA EL SALDO EN AMBAS CUENTAS
        transactionService.transferAmount(selectedAccount, destAccount, description, amount);

        // DEVUELVO LA RESPUESTA A LA WEB
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
