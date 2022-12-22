package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ColorType;

import java.util.List;

public interface CardService {
    Card getCardByNumberAndClient (String cardNumber, Client client);
    void createCard (CardType cardType, ColorType cardColor, Client client);
    void deactivateCard (Card card);
    List<CardDTO> activeCards (Client client);
}
