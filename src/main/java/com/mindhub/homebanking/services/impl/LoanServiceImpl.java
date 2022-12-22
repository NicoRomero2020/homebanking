package com.mindhub.homebanking.services.impl;

import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.ClientLoanRepository;
import com.mindhub.homebanking.repositories.LoanRepository;
import com.mindhub.homebanking.services.LoanService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanServiceImpl implements LoanService {
    @Autowired
    LoanRepository loanRepository;
    @Autowired
    ClientLoanRepository clientLoanRepository;
    @Autowired
    TransactionService transactionService;

    @Override
    public List<LoanDTO> findAllLoans() {
        return loanRepository.findAll().stream().map(LoanDTO::new).collect(Collectors.toList());
    }

    @Override
    public Loan findLoanByName(String loanName) {
        return loanRepository.findByName(loanName);
    }

    @Override
    public Loan findLoanById(Long id) {
        return loanRepository.findById(id).orElse(null);
    }

    @Override
    public void createClientLoan (Client client, Account account, Loan loan, Double amount, Integer payments) {
        Double totalAmount = amount + ( amount * loan.getRate() / 100 );
        clientLoanRepository.save(new ClientLoan(client, loan, totalAmount, payments));
        transactionService.createTransaction(TransactionType.CREDIT
                                            , loan.getName() + " - " + "Loan approved", amount, account);
    }

    @Override
    public Loan createLoan(String loanName, Double maxAmount, Short rate, ArrayList<Integer> payments) {
        return loanRepository.save(new Loan(loanName, maxAmount, payments, rate));
    }
}
