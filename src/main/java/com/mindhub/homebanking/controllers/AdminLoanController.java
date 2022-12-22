package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.NewLoanDTO;
import com.mindhub.homebanking.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminLoanController {
    @Autowired
    LoanService loanService;

    @PostMapping("/loans")
    public ResponseEntity<Object> createLoans(@RequestBody NewLoanDTO newLoan) {
        if (       newLoan.getMaxAmount() == 0
                || newLoan.getPayments().isEmpty()
                || newLoan.getRate() == 0
                || newLoan.getName().isEmpty() ) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        loanService.createLoan(newLoan.getName(), newLoan.getMaxAmount(), newLoan.getRate(), newLoan.getPayments());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
