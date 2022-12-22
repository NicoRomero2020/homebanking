package com.mindhub.homebanking.services.impl;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {
    @Autowired
    ClientRepository clientRepository;

    @Override
    public List<Client> findAllClients() {
        return clientRepository.findAll();
    }

    @Override
    public Optional<Client> findClientById(Long id) {
        return clientRepository.findById(id);
    }

    @Override
    public List<Client> findByFirstName(String firstName) {
        return clientRepository.findByFirstName(firstName);
    }

    @Override
    public List<Client> findByLastName(String lastName) {
        return clientRepository.findByLastName(lastName);
    }

    @Override
    public Client findByFirstNameAndEmail(String firstName, String email) {
        return clientRepository.findByFirstNameAndEmail(firstName,email);
    }

    @Override
    public Client findByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

    @Override
    public Client registerClient(String firstName, String lastName, String email, String password) {
        Client client = new Client(firstName, lastName, email, password);
        clientRepository.save(client);
        return client;
    }

    //TODO
    /*
    @Override
    public List<Client> findClientsByBalance (Double balance) {
    }*/
}
