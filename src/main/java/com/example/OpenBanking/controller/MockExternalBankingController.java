package com.example.OpenBanking.controller;

import com.example.OpenBanking.dto.AccountDTO;
import com.example.OpenBanking.dto.TransactionDTO;
import com.example.OpenBanking.model.Payment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/mock-external-api")
public class MockExternalBankingController {

    @GetMapping("/accounts/{accountId}/transactions")
    public ResponseEntity<List<TransactionDTO>> getMockTransactions(@PathVariable Long accountId) {
        String mockIban = "MOCKIBAN" + String.format("%010d", accountId);

        AccountDTO account = new AccountDTO();
        account.setIban(mockIban);
        List<TransactionDTO> transactions = List.of(
            new TransactionDTO(account, new BigDecimal("100.00"), "USD", "Mock payment 1", LocalDateTime.now().minusDays(1)),
            new TransactionDTO(account, new BigDecimal("200.00"), "USD", "Mock payment 2", LocalDateTime.now().minusDays(2)),
            new TransactionDTO(account, new BigDecimal("300.00"), "USD", "Mock payment 3", LocalDateTime.now().minusDays(3))
        );

        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/accounts/{accountId}/balance")
    public ResponseEntity<BigDecimal> getMockBalance(@PathVariable Long accountId) {
        return ResponseEntity.ok(new BigDecimal("10000.00"));
    }
    
}
