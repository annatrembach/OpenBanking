package com.example.OpenBanking.controller;

import com.example.OpenBanking.dto.TransactionDTO;
import com.example.OpenBanking.mapper.TransactionMapper;
import com.example.OpenBanking.model.Transaction;
import com.example.OpenBanking.model.User;
import com.example.OpenBanking.service.AccountService;
import com.example.OpenBanking.service.TransactionService;
import com.example.OpenBanking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final UserService userService;

    @GetMapping("/accounts/{accountId}/transactions")
    public ResponseEntity<List<TransactionDTO>> getLast10Transactions(@PathVariable Long accountId) {
        User currentUser = userService.getCurrentUser();

        if (!accountService.isAccountOwnedByCurrentUser(accountId, currentUser)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<Transaction> transactions = transactionService.findLast10ByAccountId(accountId);
        List<TransactionDTO> dtos = transactions.stream()
            .map(transactionMapper::toDTO)
            .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

}
