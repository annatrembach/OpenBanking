package com.example.OpenBanking.service;

import com.example.OpenBanking.model.Transaction;
import com.example.OpenBanking.model.Account;
import com.example.OpenBanking.repository.TransactionRepository;
import com.example.OpenBanking.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public Transaction create(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public List<Transaction> getAll() {
        return (List<Transaction>) transactionRepository.findAll();
    }

    public Optional<Transaction> getById(Long id) {
        return transactionRepository.findById(id);
    }

    public Transaction update(Long id, Transaction transaction) {
        Transaction existing = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        existing.setAccount(transaction.getAccount());
        existing.setAmount(transaction.getAmount());
        existing.setCurrency(transaction.getCurrency());
        existing.setDescription(transaction.getDescription());
        existing.setCreatedAt(transaction.getCreatedAt());
        return transactionRepository.save(existing);
    }

    public void delete(Long id) {
        transactionRepository.deleteById(id);
    }

    public List<Transaction> findLast10ByAccountId(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        return transactionRepository.findTop10ByAccountOrderByCreatedAtDesc(account);
    }
}
