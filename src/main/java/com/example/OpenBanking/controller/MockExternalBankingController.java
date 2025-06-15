package com.example.OpenBanking.controller;

import com.example.OpenBanking.dto.TransactionDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/mock-external-api")
public class MockExternalBankingController {

    @GetMapping("/accounts/{accountId}/balance")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable String accountId) {
        BigDecimal mockBalance = new BigDecimal("1500.75");
        System.out.println("Get balance for accountId: " + accountId);
        return ResponseEntity.ok(mockBalance);
    }

    @GetMapping("/accounts/{accountId}/transactions")
    public ResponseEntity<List<TransactionDTO>> getTransactions(@PathVariable String accountId) {
        List<TransactionDTO> transactions = List.of(
                new TransactionDTO(accountId, new BigDecimal("100.00"), "USD", "Mock payment 1", LocalDateTime.now().minusDays(1)),
                new TransactionDTO(accountId, new BigDecimal("200.50"), "USD", "Mock payment 2", LocalDateTime.now().minusDays(2)),
                new TransactionDTO(accountId, new BigDecimal("50.75"), "USD", "Mock payment 3", LocalDateTime.now().minusDays(3))
        );
        System.out.println("Get transactions for accountId: " + accountId);
        return ResponseEntity.ok(transactions);
    }

    @PostMapping("/payments/initiate")
    public ResponseEntity<Void> initiatePayment(@RequestBody com.example.OpenBanking.model.Payment payment) {
        System.out.println("Received payment: " + payment);
        return ResponseEntity.ok().build();
    }
}
