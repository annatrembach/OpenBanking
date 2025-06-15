package com.example.OpenBanking.mapper;

import com.example.OpenBanking.dto.AccountDTO;
import com.example.OpenBanking.model.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    AccountDTO toDTO(Account account);
    Account toModel(AccountDTO accountDTO);
}
