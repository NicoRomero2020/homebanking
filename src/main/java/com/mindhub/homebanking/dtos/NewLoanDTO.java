package com.mindhub.homebanking.dtos;

import java.util.ArrayList;

public class NewLoanDTO {
    private String name;
    private Double maxAmount;
    private ArrayList<Integer> payments = new ArrayList<>();
    private Short rate;

    public NewLoanDTO() {
    }

    public NewLoanDTO(String name, Double maxAmount, ArrayList<Integer> payments, Short rate) {
        this.name = name;
        this.maxAmount = maxAmount;
        this.payments = payments;
        this.rate = rate;
    }

    public String getName() {
        return name;
    }

    public Double getMaxAmount() {
        return maxAmount;
    }

    public ArrayList<Integer> getPayments() {
        return payments;
    }

    public Short getRate() { return rate; }
}

