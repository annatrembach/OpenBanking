package com.example.OpenBanking.controller;

import com.example.OpenBanking.dto.TransactionDTO;
import com.example.OpenBanking.service.AccountService;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/external/accounts")
public class ExternalController {

    private final AccountService accountService;

    @GetMapping("/{accountId}/balance")
    public ResponseEntity<BigDecimal> getExternalBalance(@PathVariable Long accountId) {
        try {
            BigDecimal balance = accountService.fetchExternalBalance(accountId);
            return ResponseEntity.ok(balance);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }

    @GetMapping("/{accountId}/transactions")
    public ResponseEntity<List<TransactionDTO>> getExternalTransactions(@PathVariable Long accountId) {
        try {
            List<TransactionDTO> transactions = accountService.fetchExternalTransactions(accountId);
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }
}
