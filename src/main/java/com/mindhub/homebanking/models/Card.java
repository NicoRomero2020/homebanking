package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Card {
    // PROPIEDADES
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "native")
    @GenericGenerator(name="native",strategy = "native")
    private Long id;
    private String cardHolder;
    private CardType type;
    private ColorType color;
    private String number;
    private Integer cvv;
    private LocalDate thruDate;
    private LocalDate fromDate;
    private boolean cardStatusActive = true;
    private boolean cardExpired = false;

    // RELACIONES
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_id")
    private Client client;

    //CONSTRUCTORES

    public Card() {
    }

    public Card(String cardHolder, CardType type, ColorType color, String number, Integer cvv, LocalDate fromDate, LocalDate thruDate, Client client) {
        this.cardHolder = cardHolder;
        this.type = type;
        this.color = color;
        this.number = number;
        this.cvv = cvv;
        this.fromDate = fromDate;
        this.thruDate = thruDate;
        this.client = client;
    }

    //GETTERS AND SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public CardType getType() {
        return type;
    }

    public void setType(CardType type) {
        this.type = type;
    }

    public ColorType getColor() {
        return color;
    }

    public void setColor(ColorType color) {
        this.color = color;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getCvv() {
        return cvv;
    }

    public void setCvv(Integer cvv) {
        this.cvv = cvv;
    }

    public LocalDate getThruDate() {
        return thruDate;
    }

    public void setThruDate(LocalDate thruDate) {
        this.thruDate = thruDate;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) { this.client = client; }

    public boolean isCardStatusActive() {
        return cardStatusActive;
    }

    public void setCardStatusActive(boolean cardStatusActive) {
        this.cardStatusActive = cardStatusActive;
    }

    public boolean isCardExpired() {
        return cardExpired;
    }

    public void setCardExpired(boolean cardExpired) {
        this.cardExpired = cardExpired;
    }
}
