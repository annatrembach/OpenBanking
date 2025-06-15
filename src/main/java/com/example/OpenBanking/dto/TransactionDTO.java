package com.example.OpenBanking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {
    private String accountIban;
    private BigDecimal amount;
    private String currency;
    private String description;
    private LocalDateTime createdAt;

}
