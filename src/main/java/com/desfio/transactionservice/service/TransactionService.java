package com.desfio.transactionservice.service;

import com.desfio.transactionservice.dto.CreateTransactionRequestDTO;
import com.desfio.transactionservice.dto.PageResponseDTO;
import com.desfio.transactionservice.dto.TransactionResponseDTO;

public interface TransactionService {

    PageResponseDTO getTransactions(String agency, Long account, Integer page, Integer size, String sort);

    TransactionResponseDTO create(CreateTransactionRequestDTO transaction);

}
