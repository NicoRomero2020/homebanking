package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDateTime;
import java.util.List;

@RepositoryRestResource
public interface AccountRepository extends JpaRepository<Account,Long> {
    List<Account> findByBalanceGreaterThan (Double balance);
    List<Account> findByCreationDateLessThan (LocalDateTime date);
    List<Account> findByOwner (Client client);
    Account findByNumber (String number);
    Account findByOwnerAndNumber (Client client,String number);
}

