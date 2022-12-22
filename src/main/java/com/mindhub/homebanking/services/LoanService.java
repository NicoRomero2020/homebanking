package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Loan;

import java.util.ArrayList;
import java.util.List;

public interface LoanService {
    List<LoanDTO> findAllLoans();
    Loan findLoanByName(String loanName);
    Loan findLoanById(Long id);
    Loan createLoan(String loanName, Double maxAmount, Short rate, ArrayList<Integer> payments);
    void createClientLoan (Client client, Account account, Loan loan, Double amount, Integer payments);

}
