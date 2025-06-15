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
    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<PaymentResponseDTO> create(@RequestBody PaymentRequestDTO dto) {
        Payment payment = paymentMapper.toModel(dto);
        Payment saved = paymentService.create(payment);
        return ResponseEntity.ok(paymentMapper.toResponseDTO(saved));
    }

    @GetMapping
    public List<PaymentResponseDTO> getAll() {
        return paymentService.findAll().stream()
                .map(paymentMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponseDTO> getById(@PathVariable Long id) {
        return paymentService.findById(id)
                .map(paymentMapper::toResponseDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentResponseDTO> update(@PathVariable Long id, @RequestBody PaymentRequestDTO dto) {
        Payment updatedPayment = paymentMapper.toModel(dto);
        Payment saved = paymentService.update(id, updatedPayment);
        return ResponseEntity.ok(paymentMapper.toResponseDTO(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        paymentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/initiate")
    public ResponseEntity<PaymentResponseDTO> initiatePayment(@RequestBody PaymentRequestDTO request) {
        Payment payment = paymentMapper.toModel(request);
        Payment savedPayment = paymentService.initiatePayment(payment);
        PaymentResponseDTO responseDTO = paymentMapper.toResponseDTO(savedPayment);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/{accountId}/external-balance")
    public ResponseEntity<BigDecimal> getExternalBalance(@PathVariable Long accountId) {
        try {
            BigDecimal balance = accountService.fetchExternalBalance(accountId);
            return ResponseEntity.ok(balance);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }
}
