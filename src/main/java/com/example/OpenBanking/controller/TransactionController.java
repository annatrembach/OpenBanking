package com.example.OpenBanking.controller;

import com.example.OpenBanking.dto.TransactionDTO;
import com.example.OpenBanking.mapper.TransactionMapper;
import com.example.OpenBanking.model.Transaction;
import com.example.OpenBanking.service.AccountService;
import com.example.OpenBanking.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;
    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<TransactionDTO> create(@RequestBody TransactionDTO dto) {
        Transaction transaction = transactionMapper.toModel(dto);
        Transaction saved = transactionService.create(transaction);
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionMapper.toDTO(saved));
    }

    @GetMapping
    public List<TransactionDTO> getAll() {
        return transactionService.getAll().stream()
                .map(transactionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDTO> getById(@PathVariable Long id) {
        return transactionService.getById(id)
                .map(transactionMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionDTO> update(@PathVariable Long id, @RequestBody TransactionDTO dto) {
        Transaction updated = transactionService.update(id, transactionMapper.toModel(dto));
        return ResponseEntity.ok(transactionMapper.toDTO(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        transactionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/accounts/{accountId}/transactions")
    public ResponseEntity<List<TransactionDTO>> getLast10Transactions(@PathVariable Long accountId) {
        List<Transaction> transactions = transactionService.findLast10ByAccountId(accountId);
        List<TransactionDTO> dtos = transactions.stream()
                .map(transactionMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/external/accounts/{accountId}/transactions")
    public ResponseEntity<List<TransactionDTO>> getExternalTransactions(@PathVariable Long accountId) {
        try {
            List<TransactionDTO> transactions = accountService.fetchExternalTransactions(accountId);
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }
}
