package com.example.OpenBanking.data;

import com.example.OpenBanking.model.*;
import com.example.OpenBanking.model.enums.PaymentStatus;
import com.example.OpenBanking.model.enums.Role;
import com.example.OpenBanking.repository.AccountRepository;
import com.example.OpenBanking.repository.PaymentRepository;
import com.example.OpenBanking.repository.TransactionRepository;
import com.example.OpenBanking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DbContext {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final PaymentRepository paymentRepository;
    private final TransactionRepository transactionRepository;
    private final PasswordEncoder passwordEncoder;

    public void initTestData() {
        if (userRepository.count() > 0 || accountRepository.count() > 0) {
            return;
        }

        User user1 = new User(null, "FirstName1", "LastName1", "1@gmail.com", passwordEncoder.encode("password1"), Role.USER);
        User user2 = new User(null, "FirstName2", "LastName2", "2@gmail.com", passwordEncoder.encode("password2"), Role.USER);
        User savedUser1 = userRepository.save(user1);
        User savedUser2 = userRepository.save(user2);

        Account acc1 = new Account(null, savedUser1, "UA12345678900000000000000001", new BigDecimal("10000.00"));
        Account acc2 = new Account(null, savedUser2, "UA12345678900000000000000002", new BigDecimal("5000.00"));
        Account savedAcc1 = accountRepository.save(acc1);
        Account savedAcc2 = accountRepository.save(acc2);

        Transaction t1 = new Transaction(null, savedAcc1, new BigDecimal("200.00"), "UAH", "description1", LocalDateTime.now().minusDays(1));
        Transaction t2 = new Transaction(null, savedAcc1, new BigDecimal("1500.00"), "UAH", "description2", LocalDateTime.now().minusDays(3));
        Transaction t3 = new Transaction(null, savedAcc2, new BigDecimal("300.00"), "UAH", "description3", LocalDateTime.now().minusDays(2));
        transactionRepository.saveAll(List.of(t1, t2, t3));

        Payment p1 = new Payment(null, savedAcc1.getIban(), savedAcc2.getIban(), new BigDecimal("1000.00"), "UAH", PaymentStatus.SUCCESS, LocalDateTime.now().minusDays(2));
        Payment p2 = new Payment(null, savedAcc2.getIban(), savedAcc1.getIban(), new BigDecimal("500.00"), "UAH", PaymentStatus.FAILED, LocalDateTime.now().minusDays(1));
        paymentRepository.saveAll(List.of(p1, p2));
    }
}
