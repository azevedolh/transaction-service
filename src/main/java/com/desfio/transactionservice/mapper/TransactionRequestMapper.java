package com.desfio.transactionservice.mapper;

import com.desfio.transactionservice.dto.CreateTransactionRequestDTO;
import com.desfio.transactionservice.model.Transaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionRequestMapper extends EntityMapper<CreateTransactionRequestDTO, Transaction> {
}
