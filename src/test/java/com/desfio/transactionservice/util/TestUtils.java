package com.desfio.transactionservice.util;

import com.desfio.transactionservice.dto.PageableResponseDTO;
import com.desfio.transactionservice.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TestUtils {
    public static PageableResponseDTO generatePageable() {
        return PageableResponseDTO.builder()
                ._limit(10)
                ._offset(0L)
                ._pageNumber(1)
                ._pageElements(1)
                ._totalPages(1)
                ._totalElements(1L)
                ._moreElements(false)
                .build();
    };

    public static List<Transaction> generateListOfTransactions() {
        List<Transaction> transactionList = new ArrayList<>();

        Transaction transaction1 = generateATransaction(
                "1234",
                1L,
                "1235",
                2L,
                new BigDecimal(100)
        );

        Transaction transaction2 = generateATransaction(
                "1234",
                1L,
                "1235",
                2L,
                new BigDecimal(100)
        );

        Transaction transaction3 = generateATransaction(
                "1234",
                1L,
                "1235",
                2L,
                new BigDecimal(100)
        );

        transactionList.add(transaction1);
        transactionList.add(transaction2);
        transactionList.add(transaction3);

        return transactionList;
    }

    public static Transaction generateATransaction(
            String originAgency,
            Long originAccount,
            String destinationAgency,
            Long destinationAccount,
            BigDecimal amount) {

        return Transaction.builder()
                .id(UUID.randomUUID())
                .originCustomerId(UUID.randomUUID())
                .originAgency(originAgency)
                .originAccount(originAccount)
                .destinationCustomerId(UUID.randomUUID())
                .destinationAgency(destinationAgency)
                .destinationAccount(destinationAccount)
                .amount(amount)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
