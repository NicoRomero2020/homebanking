package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDateTime;
import java.util.List;

@RepositoryRestResource
public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    List<Transaction> findByDateBetween (LocalDateTime date1, LocalDateTime date2);

    List<Transaction> findByAmountBetween (Double amount1, Double amount2);

    List<Transaction> findByType (String type);
}
