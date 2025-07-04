package com.example.OpenBanking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDTO {
    private String consumerIban;
    private String receiverIban;
    private BigDecimal amount;
    private String currency;
}
