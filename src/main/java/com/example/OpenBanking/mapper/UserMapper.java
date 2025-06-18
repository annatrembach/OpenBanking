package com.example.OpenBanking.mapper;

import com.example.OpenBanking.dto.UserDTO;
import com.example.OpenBanking.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toModel(UserDTO dto);
    UserDTO toDTO(User user);
}
