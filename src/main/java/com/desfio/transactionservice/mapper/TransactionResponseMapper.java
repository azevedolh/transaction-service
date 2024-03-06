package com.desfio.transactionservice.mapper;


import com.desfio.transactionservice.dto.TransactionResponseDTO;
import com.desfio.transactionservice.model.Transaction;

import java.util.List;

public interface TransactionResponseMapper {
    TransactionResponseDTO toDto(Transaction transaction, String agency, Long account);

    List<TransactionResponseDTO> toDto(List<Transaction> transactionList, String agency, Long account);
}
