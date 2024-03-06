package com.desfio.transactionservice.mapper.impl;

import com.desfio.transactionservice.dto.TransactionResponseDTO;
import com.desfio.transactionservice.enumerator.OperationEnum;
import com.desfio.transactionservice.mapper.TransactionResponseMapper;
import com.desfio.transactionservice.model.Transaction;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TransactionResponseMapperImpl implements TransactionResponseMapper {

    @Override
    public TransactionResponseDTO toDto(Transaction entity, String agency, Long account) {
        if ( entity == null ) {
            return null;
        }

        TransactionResponseDTO.TransactionResponseDTOBuilder transactionResponseDTO = TransactionResponseDTO.builder();

        transactionResponseDTO.id( entity.getId() );
        transactionResponseDTO.amount( entity.getAmount() );
        transactionResponseDTO.createdAt( entity.getCreatedAt() );
        transactionResponseDTO.updatedAt( entity.getUpdatedAt() );
        transactionResponseDTO.originCustomerId( entity.getOriginCustomerId() );
        transactionResponseDTO.originAgency( entity.getOriginAgency() );
        transactionResponseDTO.originAccount( entity.getOriginAccount() );
        transactionResponseDTO.destinationCustomerId( entity.getDestinationCustomerId() );
        transactionResponseDTO.destinationAgency(entity.getDestinationAgency() );
        transactionResponseDTO.destinationAccount( entity.getDestinationAccount() );

        if ( entity.getDestinationAgency().equals(agency) && entity.getDestinationAccount().equals(account)) {
            transactionResponseDTO.operation(OperationEnum.CREDITO);
        }

        if ( entity.getOriginAgency().equals(agency) && entity.getOriginAccount().equals(account)) {
            transactionResponseDTO.operation(OperationEnum.DEBITO);
        }

        return transactionResponseDTO.build();
    }

    @Override
    public List<TransactionResponseDTO> toDto(List<Transaction> dtoList, String agency, Long account) {
        if ( dtoList == null && account != null) {
            return null;
        }

        List<TransactionResponseDTO> list = new ArrayList<TransactionResponseDTO>( dtoList.size() );
        for ( Transaction transaction : dtoList ) {
            list.add( toDto( transaction, agency, account ) );
        }

        return list;
    }
}
