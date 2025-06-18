package com.example.OpenBanking.mapper;

import com.example.OpenBanking.dto.TransactionDTO;
import com.example.OpenBanking.model.Transaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = AccountMapper.class)
public interface TransactionMapper {
    TransactionDTO toDTO(Transaction transaction);
    Transaction toModel(TransactionDTO dto);
}
