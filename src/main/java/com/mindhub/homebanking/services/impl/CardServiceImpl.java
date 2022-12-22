package com.mindhub.homebanking.services.impl;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ColorType;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static com.mindhub.homebanking.utils.CardUtils.getCardNumber;
import static com.mindhub.homebanking.utils.CardUtils.getCvvNumber;
import static java.util.stream.Collectors.toList;

@Service
public class CardServiceImpl implements CardService {
    @Autowired
    CardRepository cardRepository;

    @Override
    public Card getCardByNumberAndClient (String cardNumber, Client client) {
        return cardRepository.findByNumberAndClient(cardNumber, client);
    };
    @Override
    public void createCard (CardType cardType, ColorType cardColor, Client client) {
        String cardHolder = client.getFirstName() + " " + client.getLastName();

        Integer cvvNumber = getCvvNumber();

        String cardNumber = getCardNumber();

        LocalDate fromDate = LocalDate.now();

        LocalDate thruDate = fromDate.plusYears(5);

        cardRepository.save(new Card(cardHolder, cardType, cardColor, cardNumber, cvvNumber, fromDate, thruDate, client));
    }
    @Override
    public void deactivateCard (Card card) {
        card.setCardStatusActive(false);
        cardRepository.save(card);
    }
    @Override
    public List<CardDTO> activeCards (Client client) {
        for (Card card : client.getCards()) {
            if (LocalDate.now().isAfter(card.getThruDate())) {
                card.setColor(ColorType.EXPIRED);
                card.setCardStatusActive(false);
            }
        }
        return client.getCards().stream().filter(Card::isCardStatusActive).map(CardDTO::new).collect(toList());
    }
}
