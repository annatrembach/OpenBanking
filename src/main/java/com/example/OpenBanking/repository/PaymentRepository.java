package com.example.OpenBanking.repository;

import com.example.OpenBanking.model.Payment;
import org.springframework.data.repository.CrudRepository;

public interface PaymentRepository extends CrudRepository<Payment, Long> {
}
