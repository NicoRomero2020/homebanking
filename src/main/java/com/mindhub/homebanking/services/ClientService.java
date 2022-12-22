package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Client;

import java.util.List;
import java.util.Optional;

public interface ClientService {
    List<Client> findAllClients();
    Optional<Client> findClientById(Long id);
    List<Client> findByFirstName (String firstName);
    List<Client> findByLastName (String lastName);
    Client findByFirstNameAndEmail (String firstName, String email);
    Client findByEmail (String email);
    Client registerClient (String firstName, String lastName, String email, String password);
}
