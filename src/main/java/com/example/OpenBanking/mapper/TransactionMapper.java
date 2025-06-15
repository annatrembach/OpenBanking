package com.example.OpenBanking.mapper;

import com.example.OpenBanking.dto.TransactionDTO;
import com.example.OpenBanking.model.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(source = "account.iban", target = "accountIban")
    TransactionDTO toDTO(Transaction transaction);

    @Mapping(source = "accountIban", target = "account.iban")
    Transaction toModel(TransactionDTO transactionDTO);
}
