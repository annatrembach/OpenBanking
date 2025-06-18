package com.example.OpenBanking.repository;

import com.example.OpenBanking.model.Account;
import com.example.OpenBanking.model.User;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AccountRepository extends CrudRepository<Account, Long> {
    Optional<Account> findByIban(String iban);
    Optional<Account> findTopByOrderByIdDesc();
    List<Account> findByUser(User user);
}
