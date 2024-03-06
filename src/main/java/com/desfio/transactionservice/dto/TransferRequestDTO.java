package com.desfio.transactionservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransferRequestDTO {
    @NotNull
    private Long originAccount;

    @NotNull
    private String originAgency;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private Long destinationAccount;

    @NotNull
    private String destinationAgency;
}
