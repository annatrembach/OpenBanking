package com.example.OpenBanking.repository;

import com.example.OpenBanking.model.Account;
import com.example.OpenBanking.model.Transaction;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    List<Transaction> findTop10ByAccountOrderByCreatedAtDesc(Account account);
}
