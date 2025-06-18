package com.example.OpenBanking.service;

import com.example.OpenBanking.model.Transaction;
import com.example.OpenBanking.model.Account;
import com.example.OpenBanking.model.User;
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

    public List<Transaction> findLast10ByAccountId(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        return transactionRepository.findTop10ByAccountOrderByCreatedAtDesc(account);
    }
}
