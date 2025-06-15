package com.example.OpenBanking.service;

import com.example.OpenBanking.dto.TransactionDTO;
import com.example.OpenBanking.model.Account;
import com.example.OpenBanking.repository.AccountRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final ExternalBankingClient externalBankingClient;

    public Optional<Account> findByIban(String iban) {
        return accountRepository.findByIban(iban);
    }

    public Optional<Account> findById(Long id) {
        return accountRepository.findById(id);
    }

    public List<Account> findAll() {
        return (List<Account>) accountRepository.findAll();
    }

    public Account save(Account account) {
        return accountRepository.save(account);
    }

    public Account update(Long id, Account updatedAccount) {
        return accountRepository.findById(id).map(existing -> {
            existing.setIban(updatedAccount.getIban());
            existing.setBalance(updatedAccount.getBalance());
            existing.setUser(updatedAccount.getUser());
            return accountRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Account not found"));
    }

    public void deleteById(Long id) {
        accountRepository.deleteById(id);
    }

    public BigDecimal fetchExternalBalance(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        return externalBankingClient.getBalance(account.getIban());
    }

    public List<TransactionDTO> fetchExternalTransactions(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        return externalBankingClient.getTransactions(account.getIban());
    }

    public BigDecimal fetchBalance(String iban) {
        return accountRepository.findByIban(iban)
                .map(Account::getBalance)
                .orElse(BigDecimal.ZERO);
    }

    @Transactional
    public void decreaseBalance(String iban, BigDecimal amount) {
        Account account = accountRepository.findByIban(iban)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);
    }
}
