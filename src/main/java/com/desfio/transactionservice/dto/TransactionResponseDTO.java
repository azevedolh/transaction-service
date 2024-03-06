package com.desfio.transactionservice.dto;

import com.desfio.transactionservice.enumerator.OperationEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import jdk.dynalink.Operation;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponseDTO {
    private UUID id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private OperationEnum operation;
    private UUID originCustomerId;
    private Long originAccount;
    private String originAgency;
    private UUID destinationCustomerId;
    private Long destinationAccount;
    private String destinationAgency;
    private BigDecimal amount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
