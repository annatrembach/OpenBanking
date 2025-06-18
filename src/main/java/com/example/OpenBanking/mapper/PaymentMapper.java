package com.example.OpenBanking.mapper;

import com.example.OpenBanking.dto.PaymentRequestDTO;
import com.example.OpenBanking.dto.PaymentResponseDTO;
import com.example.OpenBanking.model.Payment;
import com.example.OpenBanking.model.enums.PaymentStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Payment toModel(PaymentRequestDTO dto);

    PaymentResponseDTO toResponseDTO(Payment payment);

}


