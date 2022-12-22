package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CardApplicationDTO;
import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CardController {
    @Autowired
    private CardService cardService;
    @Autowired
    private ClientService clientService;

    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> createCard(Authentication authentication
                                        , @RequestParam ColorType cardColor
                                        , @RequestParam CardType cardType) {
        Client loggedClient = clientService.findByEmail(authentication.getName());

        if (loggedClient.getCards().stream().filter((card -> cardType.equals(card.getType()))).count() >= 3) {
            return new ResponseEntity<>("LIMITE MAXIMO DE TARJETAS SUPERADO", HttpStatus.FORBIDDEN);
        }

        cardService.createCard(cardType, cardColor, loggedClient);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/clients/validate/card")
    public ResponseEntity<Object> validateClientCard (@RequestBody CardApplicationDTO paymentCard) {
        Client loggedClient = clientService.findByEmail(paymentCard.getEmail());
        List<CardDTO> activeCards = cardService.activeCards(loggedClient);
        for (CardDTO card: activeCards) {
            String paymentThruDate = paymentCard.getMesVencimiento().toString() + paymentCard.getAnioVencimiento().toString();
            String cardNumber = card.getNumber().replace("-"," ");
            String paymentNumber = paymentCard.getNumTarjeta();
            if (cardNumber.equals(paymentNumber)
                && card.getCvv() == paymentCard.getCvv()
                && card.getThruDate().format(DateTimeFormatter.ofPattern("MMyy")).equals(paymentThruDate)) {
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.CONTINUE);
    }

    @GetMapping("/clients/current/cards")
    public List<CardDTO> getCardsByClient(Authentication authentication) {
        Client loggedClient = clientService.findByEmail(authentication.getName());
        return cardService.activeCards(loggedClient);
    }

    @DeleteMapping("/clients/current/cards")
    public ResponseEntity<Object> deleteCardsByClient(Authentication authentication
    , @RequestParam String cardNumber) {
        Client loggedClient = clientService.findByEmail(authentication.getName());
        Card card = cardService.getCardByNumberAndClient(cardNumber, loggedClient);
        cardService.deactivateCard(card);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
