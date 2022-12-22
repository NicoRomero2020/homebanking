package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.stream.DoubleStream;

@RepositoryRestResource
public interface ClientRepository extends JpaRepository<Client,Long> {
    List<Client> findByFirstName (String firstName);
    List<Client> findByLastName (String lastName);
    Client findByFirstNameAndEmail (String firstName, String email);
    Client findByEmail (String email);
}
