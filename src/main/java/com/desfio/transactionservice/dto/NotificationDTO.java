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
public class NotificationDTO {

    private UUID customerId;
    private String agency;
    private Long account;
    private String subject;
    private String message;
}
