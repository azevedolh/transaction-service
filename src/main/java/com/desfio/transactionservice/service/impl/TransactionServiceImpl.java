package com.desfio.transactionservice.service.impl;

import com.desfio.transactionservice.dto.*;
import com.desfio.transactionservice.exception.CustomBusinessException;
import com.desfio.transactionservice.mapper.PageableMapper;
import com.desfio.transactionservice.mapper.TransactionRequestMapper;
import com.desfio.transactionservice.mapper.TransactionResponseMapper;
import com.desfio.transactionservice.model.Transaction;
import com.desfio.transactionservice.producer.NotificationProducer;
import com.desfio.transactionservice.repository.TransactionRepository;
import com.desfio.transactionservice.service.AccountService;
import com.desfio.transactionservice.service.TransactionService;
import com.desfio.transactionservice.util.MessageUtil;
import com.desfio.transactionservice.util.PaginationUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static com.desfio.transactionservice.util.ConstantUtil.SORT_BY_CREATED_AT;


@Log4j2
@Service
public class TransactionServiceImpl implements TransactionService {

    private TransactionRepository transactionRepository;
    private PageableMapper pageableMapper;
    private TransactionResponseMapper transactionResponseMapper;
    private TransactionRequestMapper transactionRequestMapper;
    private AccountService accountService;

    private NotificationProducer notificationProducer;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository,
                                  PageableMapper pageableMapper,
                                  TransactionResponseMapper transactionResponseMapper,
                                  TransactionRequestMapper transactionRequestMapper,
                                  AccountService accountService,
                                  NotificationProducer notificationProducer) {
        this.transactionRepository = transactionRepository;
        this.pageableMapper = pageableMapper;
        this.transactionResponseMapper = transactionResponseMapper;
        this.transactionRequestMapper = transactionRequestMapper;
        this.accountService = accountService;
        this.notificationProducer = notificationProducer;
    }

    @Override
    public PageResponseDTO getTransactions(String agency, Long account, Integer page, Integer size, String sort) {

        Sort sortProperties = PaginationUtil.getSort(sort, Sort.Direction.DESC, SORT_BY_CREATED_AT);

        PageRequest pageRequest = PageRequest.of(page - 1, size, sortProperties);
        PageResponseDTO pageResponseDTO = new PageResponseDTO();
        Page<Transaction> transactionPage = transactionRepository.findTransactionsByAgencyAndAccount(pageRequest, agency, account);

        if (transactionPage != null) {
            pageResponseDTO.set_pageable(pageableMapper.toDto(transactionPage));
            pageResponseDTO.set_content(transactionResponseMapper.toDto(transactionPage.getContent(), agency, account));
        }

        return pageResponseDTO;
    }

    @Override
    @Transactional
    public TransactionResponseDTO create(CreateTransactionRequestDTO transactionRequest) {

        if (transactionRequest.getDestinationAccount().equals(transactionRequest.getOriginAccount()) &&
                transactionRequest.getDestinationAgency().equals(transactionRequest.getOriginAgency())) {
            String message = MessageUtil.getMessage("transaction.not.permited");
            String details = MessageUtil.getMessage("transaction.origin.destination");
            throw new CustomBusinessException(message, details);
        }

        TransferRequestDTO request = TransferRequestDTO.builder()
                .originAgency(transactionRequest.getOriginAgency())
                .originAccount(transactionRequest.getOriginAccount())
                .destinationAgency(transactionRequest.getDestinationAgency())
                .destinationAccount(transactionRequest.getDestinationAccount())
                .amount(transactionRequest.getAmount())
                .build();

        try {
            accountService.transfer(request);
        } catch (CustomBusinessException e) {
            log.error("Erro ao realizar processo de transferência", e);
            throw e;
        }

        Transaction transaction = transactionRequestMapper.toEntity(transactionRequest);
        transaction = transactionRepository.saveAndFlush(transaction);
        TransactionResponseDTO transactionResponseDTO = transactionResponseMapper.toDto(
                transaction,
                null,
                null
        );

        String message = MessageUtil.getMessage(
                "transaction.origin.message",
                transaction.getOriginAgency(),
                transaction.getOriginAccount().toString(),
                transaction.getAmount().toString(),
                transaction.getDestinationAgency(),
                transaction.getDestinationAccount().toString()
        );

        String subject = MessageUtil.getMessage("transaction.subject");

        NotificationDTO originNotification = NotificationDTO.builder()
                .customerId(transaction.getOriginCustomerId())
                .agency(transaction.getOriginAgency())
                .account(transaction.getOriginAccount())
                .subject(subject)
                .message(message)
                .build();

        message = MessageUtil.getMessage(
                "transaction.destination.message",
                transaction.getDestinationAgency(),
                transaction.getDestinationAccount().toString(),
                transaction.getAmount().toString(),
                transaction.getOriginAgency(),
                transaction.getOriginAccount().toString()
        );

        NotificationDTO destinationNotification = NotificationDTO.builder()
                .customerId(transaction.getDestinationCustomerId())
                .agency(transaction.getDestinationAgency())
                .account(transaction.getDestinationAccount())
                .subject(subject)
                .message(message)
                .build();

        sendNotification(originNotification);
        sendNotification(destinationNotification);

        return transactionResponseDTO;
    }

    private void sendNotification(NotificationDTO notification) {
        try {
            notificationProducer.sendMessage(notification);
        } catch (JsonProcessingException e) {
            String message = MessageUtil.getMessage("transaction.message.conversion.error");
            throw new CustomBusinessException(HttpStatus.INTERNAL_SERVER_ERROR, message, e.getMessage());
        }
    }

    private Transaction getById(UUID id) {
        Optional<Transaction> transactionOptional = transactionRepository.findById(id);

        if (transactionOptional.isEmpty()) {
            String message = MessageUtil.getMessage("transaction.not.found");
            throw new CustomBusinessException(HttpStatus.NOT_FOUND, message);
        }

        return transactionOptional.get();
    }
}
