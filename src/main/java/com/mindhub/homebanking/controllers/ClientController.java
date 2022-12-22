package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class ClientController {
    @Autowired
    ClientService clientService;
    @Autowired
    AccountService accountService;
    @Autowired PasswordEncoder passwordEncoder;

    @PostMapping("/clients")
    public ResponseEntity<Object> registerClient(
              @RequestParam String firstName
            , @RequestParam String lastName
            , @RequestParam String email
            , @RequestParam String password
            , Authentication authentication) {
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        if (clientService.findByEmail(email) !=  null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }
        Client newClient = clientService.registerClient(firstName, lastName, email, passwordEncoder.encode(password));

        accountService.createAccount(newClient, AccountType.SAVINGS);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/clients/current")
    public ClientDTO getClientByEmail(Authentication authentication){
        return new ClientDTO(clientService.findByEmail(authentication.getName()));
    }

    @GetMapping("/clients")
    public List<ClientDTO> getClients(){
        return clientService.findAllClients().stream().map(ClientDTO::new).collect(toList());
    }

    @GetMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable long id){
        return clientService.findClientById(id).map(ClientDTO::new).orElse(null);
    }

    @GetMapping("/clients/fname/{firstName}")
    public List<ClientDTO> getClientByFirstName(@PathVariable String firstName){
        return clientService.findByFirstName(firstName).stream().map(ClientDTO::new).collect(toList());
    }

    @GetMapping("/clients/lname/{lastName}")
    public List<ClientDTO> getClientByLastName(@PathVariable String lastName){
        return clientService.findByLastName(lastName).stream().map(ClientDTO::new).collect(toList());
    }

    @GetMapping("/clients/fname/{firstName}/{email}")
    public ClientDTO getClientByNameAndEmail(@PathVariable String firstName, @PathVariable String email){
        Client client = clientService.findByFirstNameAndEmail(firstName, email);
        return new ClientDTO(client);
    }
}
