package com.example.OpenBanking.controller;

import com.example.OpenBanking.dto.PaymentRequestDTO;
import com.example.OpenBanking.dto.PaymentResponseDTO;
import com.example.OpenBanking.mapper.PaymentMapper;
import com.example.OpenBanking.model.Payment;
import com.example.OpenBanking.service.AccountService;
import com.example.OpenBanking.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final PaymentMapper paymentMapper;

    @PostMapping("/initiate")
    public ResponseEntity<PaymentResponseDTO> initiatePayment(@RequestBody PaymentRequestDTO request) {
        Payment payment = paymentMapper.toModel(request);
        Payment savedPayment = paymentService.initiatePayment(payment);
        PaymentResponseDTO responseDTO = paymentMapper.toResponseDTO(savedPayment);
        return ResponseEntity.ok(responseDTO);
    }

}
