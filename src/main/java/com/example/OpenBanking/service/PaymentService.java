package com.example.OpenBanking.service;

import com.example.OpenBanking.model.Payment;
import com.example.OpenBanking.model.enums.PaymentStatus;
import com.example.OpenBanking.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ExternalBankingClient externalBankingClient;
    private final AccountService accountService;

    public Payment create(Payment payment) {
        payment.setStatus(PaymentStatus.PENDING);
        payment.setCreatedAt(LocalDateTime.now());
        return paymentRepository.save(payment);
    }

    public List<Payment> findAll() {
        return (List<Payment>) paymentRepository.findAll();
    }

    public Optional<Payment> findById(Long id) {
        return paymentRepository.findById(id);
    }

    public Payment update(Long id, Payment updated) {
        return paymentRepository.findById(id).map(p -> {
            p.setAmount(updated.getAmount());
            p.setCurrency(updated.getCurrency());
            p.setConsumerIban(updated.getConsumerIban());
            p.setReceiverIban(updated.getReceiverIban());
            p.setStatus(updated.getStatus());
            return paymentRepository.save(p);
        }).orElseThrow(() -> new RuntimeException("Payment not found"));
    }

    public void delete(Long id) {
        paymentRepository.deleteById(id);
    }

    @Transactional
    public Payment initiatePayment(Payment payment) {
        BigDecimal currentBalance = accountService.fetchBalance(payment.getConsumerIban());
        if (currentBalance.compareTo(payment.getAmount()) < 0) {
            payment.setStatus(PaymentStatus.FAILED);
            payment.setCreatedAt(LocalDateTime.now());
            return paymentRepository.save(payment);
        }

        payment.setStatus(PaymentStatus.PENDING);
        payment.setCreatedAt(LocalDateTime.now());
        Payment savedPayment = paymentRepository.save(payment);

        boolean success = false;
        try {
            success = externalBankingClient.sendPayment(savedPayment);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (success) {
            savedPayment.setStatus(PaymentStatus.SUCCESS);
            accountService.decreaseBalance(payment.getConsumerIban(), payment.getAmount());
        } else {
            savedPayment.setStatus(PaymentStatus.FAILED);
        }

        return paymentRepository.save(savedPayment);
    }
}
