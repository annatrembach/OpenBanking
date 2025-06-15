package com.example.OpenBanking.controller;

import com.example.OpenBanking.dto.AccountDTO;
import com.example.OpenBanking.mapper.AccountMapper;
import com.example.OpenBanking.model.Account;
import com.example.OpenBanking.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final AccountMapper accountMapper;

    @GetMapping
    public List<AccountDTO> getAllAccounts() {
        return accountService.findAll().stream()
                .map(accountMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDTO> getById(@PathVariable Long id) {
        return accountService.findById(id)
                .map(accountMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{accountId}/balance")
    public ResponseEntity<BigDecimal> getBalanceByAccountId(@PathVariable Long accountId) {
        return accountService.findById(accountId)
                .map(Account::getBalance)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AccountDTO> createAccount(@RequestBody AccountDTO dto) {
        Account account = accountMapper.toModel(dto);
        Account saved = accountService.save(account);
        return ResponseEntity.ok(accountMapper.toDTO(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountDTO> updateAccount(@PathVariable Long id, @RequestBody AccountDTO dto) {
        Account account = accountMapper.toModel(dto);
        Account updated = accountService.update(id, account);
        return ResponseEntity.ok(accountMapper.toDTO(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        accountService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
