package com.example.OpenBanking.mapper;

import com.example.OpenBanking.dto.AccountDTO;
import com.example.OpenBanking.model.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface AccountMapper {
    Account toModel(AccountDTO dto);
    AccountDTO toDTO(Account account);
}