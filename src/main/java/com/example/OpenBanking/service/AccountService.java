package com.example.OpenBanking.service;

import com.example.OpenBanking.dto.TransactionDTO;
import com.example.OpenBanking.model.Account;
import com.example.OpenBanking.model.User;
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

    public Optional<Account> findById(Long id) {
        return accountRepository.findById(id);
    }

    public List<Account> findByUser(User user) {
        return accountRepository.findByUser(user);
    }

    public Account save(Account account) {
        return accountRepository.save(account);
    }

    public Long getLastAccountId() {
        return accountRepository.findTopByOrderByIdDesc()
            .map(Account::getId)
            .orElse(0L);
    }

    public String generateIban(Long accountNumber) {
        String countryCode = "UA";
        String checksum = "00";
        String bankCode = "12345";
        String accountPart = String.format("%019d", accountNumber);

        return countryCode + checksum + bankCode + accountPart;
    }

    public Account update(Long id, Account accountDto) {
        return accountRepository.findById(id).map(existing -> {
            User user = existing.getUser();

            if (accountDto.getUser() != null) {
                User dtoUser = accountDto.getUser();
                if (dtoUser.getFirstName() != null) user.setFirstName(dtoUser.getFirstName());
                if (dtoUser.getLastName() != null) user.setLastName(dtoUser.getLastName());
                if (dtoUser.getEmail() != null) user.setEmail(dtoUser.getEmail());
            }

            return accountRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Account not found"));
    }

    public void deleteById(Long id) {
        accountRepository.deleteById(id);
    }

    public Optional<Account> findByIban(String fromIban) {
        return accountRepository.findByIban(fromIban);
    }

    public boolean isAccountOwnedByCurrentUser(Long accountId, User user) {
        return accountRepository.findById(accountId)
            .map(account -> account.getUser().getId().equals(user.getId()))
            .orElse(false);
    }

    public BigDecimal fetchExternalBalance(Long accountId) {
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new RuntimeException("Account not found"));
        return externalBankingClient.getBalance(account.getId());
    }

    public List<TransactionDTO> fetchExternalTransactions(Long accountId) {
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new RuntimeException("Account not found"));
        return externalBankingClient.getTransactions(account.getId());
    }
}
