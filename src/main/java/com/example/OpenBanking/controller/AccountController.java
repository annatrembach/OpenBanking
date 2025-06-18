package com.example.OpenBanking.controller;

import com.example.OpenBanking.dto.AccountDTO;
import com.example.OpenBanking.mapper.AccountMapper;
import com.example.OpenBanking.model.Account;
import com.example.OpenBanking.model.User;
import com.example.OpenBanking.service.AccountService;
import com.example.OpenBanking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    private final UserService userService;

    @GetMapping("/")
    public List<AccountDTO> getAllAccountsForCurrentUser() {
        User currentUser = userService.getCurrentUser();
        return accountService.findByUser(currentUser).stream()
            .map(accountMapper::toDTO)
            .collect(Collectors.toList());
    }

    @PostMapping("/create")
    public ResponseEntity<AccountDTO> createAccountForCurrentUser() {
        User currentUser = userService.getCurrentUser();

        Long lastId = accountService.getLastAccountId();
        String newIban = accountService.generateIban(lastId + 1);

        Account account = new Account();
        account.setUser(currentUser);
        account.setIban(newIban);
        account.setBalance(BigDecimal.ZERO);

        Account saved = accountService.save(account);
        return ResponseEntity.ok(accountMapper.toDTO(saved));
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
