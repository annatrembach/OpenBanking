package com.example.OpenBanking.service;

import com.example.OpenBanking.model.Account;
import com.example.OpenBanking.model.Payment;
import com.example.OpenBanking.model.Transaction;
import com.example.OpenBanking.model.User;
import com.example.OpenBanking.model.enums.PaymentStatus;
import com.example.OpenBanking.repository.PaymentRepository;
import com.example.OpenBanking.repository.TransactionRepository;
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
    private final TransactionService transactionService;
    private final TransactionRepository transactionRepository;
    private final UserService userService;

    @Transactional
    public Payment initiatePayment(Payment payment) {
        User currentUser = userService.getCurrentUser();

        Account fromAccount = accountService.findByIban(payment.getConsumerIban())
            .orElseThrow(() -> new RuntimeException("Sender account not found"));

        if (!fromAccount.getUser().getId().equals(currentUser.getId())) {
            payment.setStatus(PaymentStatus.FAILED);
            payment.setCreatedAt(LocalDateTime.now());
            return paymentRepository.save(payment);
        }

        BigDecimal currentBalance = fromAccount.getBalance();
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
            processInternalPaymentTransactions(savedPayment);
        } else {
            savedPayment.setStatus(PaymentStatus.FAILED);
        }

        return paymentRepository.save(savedPayment);
    }

    @Transactional
    public void processInternalPaymentTransactions(Payment payment) {
        if (payment.getStatus() != PaymentStatus.SUCCESS) {
            return;
        }

        String fromIban = payment.getConsumerIban();
        String toIban = payment.getReceiverIban();
        BigDecimal amount = payment.getAmount();

        Optional<Account> fromOpt = accountService.findByIban(fromIban);
        Optional<Account> toOpt = accountService.findByIban(toIban);

        if (fromOpt.isPresent()) {
            Account fromAccount = fromOpt.get();

            fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
            accountService.save(fromAccount);

            Transaction senderTx = new Transaction();
            senderTx.setAccount(fromAccount);
            senderTx.setAmount(amount.negate());
            senderTx.setCurrency(payment.getCurrency());
            senderTx.setDescription("Payment to " + toIban);
            senderTx.setCreatedAt(LocalDateTime.now());
            transactionRepository.save(senderTx);
        }

        if (toOpt.isPresent()) {
            Account toAccount = toOpt.get();

            toAccount.setBalance(toAccount.getBalance().add(amount));
            accountService.save(toAccount);

            Transaction receiverTx = new Transaction();
            receiverTx.setAccount(toAccount);
            receiverTx.setAmount(amount);
            receiverTx.setCurrency(payment.getCurrency());
            receiverTx.setDescription("Received from " + fromIban);
            receiverTx.setCreatedAt(LocalDateTime.now());
            transactionRepository.save(receiverTx);
        }
    }

}
