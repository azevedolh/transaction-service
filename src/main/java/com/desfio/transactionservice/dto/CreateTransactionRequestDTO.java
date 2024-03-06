package com.desfio.transactionservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateTransactionRequestDTO {

    @NotNull
    private UUID originCustomerId;

    @NotNull
    private Long originAccount;

    @NotNull
    private String originAgency;

    @NotNull
    private UUID destinationCustomerId;

    @NotNull
    private Long destinationAccount;

    @NotNull
    private String destinationAgency;

    @NotNull
    private BigDecimal amount;
}
