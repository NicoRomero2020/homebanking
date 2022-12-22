package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository
									, AccountRepository accountRepository
									, TransactionRepository transactionRepository
									, LoanRepository loanRepository
									, ClientLoanRepository clientLoanRepository
									, CardRepository cardRepository) {
		return (args) -> {
			Client client0 = new Client("admin", "admin", "admin@mail.com",passwordEncoder.encode("5678"));
			clientRepository.save(client0);

			Client client1 = new Client("Nico", "Romero", "nico.romero@mail.com",passwordEncoder.encode("1234"));
			clientRepository.save(client1);

			Client client2 = new Client("Noe", "Martinez", "noe.martinez@mail.com",passwordEncoder.encode("1234"));
			clientRepository.save(client2);

			Account account1 = new Account("CA001", LocalDateTime.now() , 1000.00, AccountType.SAVINGS, client1);
			accountRepository.save(account1);

			Account account2 = new Account("CA002", LocalDateTime.now() , 1000.00, AccountType.CURRENT, client1);
			accountRepository.save(account2);

			Account account3 = new Account("CA003", LocalDateTime.now() , 1000.00, AccountType.SAVINGS, client2);
			accountRepository.save(account3);

			Transaction transaction1 = new Transaction(TransactionType.CREDIT
					,"PAGO DE HABERES"
					, LocalDateTime.now()
					, 50000.00
					, 50000.00
					, account1);
			transactionRepository.save(transaction1);

			Transaction transaction2 = new Transaction(TransactionType.DEBIT
					,"DEBITO AUT. TARJETA"
					, LocalDateTime.now()
					, -1000.00
					, -1000.00
					, account1);
			transactionRepository.save(transaction2);

			Transaction transaction3 = new Transaction(TransactionType.DEBIT
					,"DEBITO PRESTAMO"
					, LocalDateTime.now()
					, -1000.00
					, -1000.00
					, account1);
			transactionRepository.save(transaction3);

			Transaction transaction4 = new Transaction(TransactionType.DEBIT
					,"CREDITO SEGURO"
					, LocalDateTime.now()
					, 5000.00
					, 5000.00
					, account1);
			transactionRepository.save(transaction4);

			Loan loan1 = new Loan("PERSONAL", 10000.00, List.of(12,24), (short) 76);
			loanRepository.save(loan1);
			Loan loan2 = new Loan("PERSONAL", 100000.00, List.of(24,36), (short) 86);
			loanRepository.save(loan2);
			Loan loan3 = new Loan("PRENDARIO", 1000000.00, List.of(36,48), (short) 110);
			loanRepository.save(loan3);

			Card card1 = new Card("NICOLAS ANDRES ROMERO"
					, CardType.DEBIT
					, ColorType.GOLD
					, "1234-5678-9012-3456"
					, 123
					, LocalDate.now()
					, LocalDate.now().minusYears(5)
					, client1);
			cardRepository.save(card1);

			Card card2 = new Card("NICOLAS ANDRES ROMERO"
					, CardType.CREDIT
					, ColorType.TITANIUM
					, "1234-5678-9098-7654"
					, 123
					, LocalDate.now()
					, LocalDate.now().plusYears(5)
					, client1);
			cardRepository.save(card2);
			/*
			ClientLoan clientLoan1 = new ClientLoan(client1, loan1, 5000.00,24);
			clientLoanRepository.save(clientLoan1);
			ClientLoan clientLoan2 = new ClientLoan(client1, loan2, 10000.00,24);
			clientLoanRepository.save(clientLoan1);

			 */
		};
	}
}