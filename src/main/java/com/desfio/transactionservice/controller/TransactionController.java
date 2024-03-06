package com.desfio.transactionservice.controller;

import com.desfio.transactionservice.dto.CreateTransactionRequestDTO;
import com.desfio.transactionservice.dto.PageResponseDTO;
import com.desfio.transactionservice.dto.TransactionResponseDTO;
import com.desfio.transactionservice.service.TransactionService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Log4j2
@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping()
    public ResponseEntity<PageResponseDTO> getAll(
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(value = "_sort", required = false) String sort,
            @RequestParam(value = "agency") String agency,
            @RequestParam(value = "account") Long account) {
        return new ResponseEntity<PageResponseDTO>(transactionService.getTransactions(
                agency,
                account,
                page,
                size,
                sort),
                HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<TransactionResponseDTO> create(
            @RequestBody @Valid CreateTransactionRequestDTO transaction) {
        TransactionResponseDTO createdTransaction = transactionService.create(transaction);

        URI locationResource = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdTransaction.getId())
                .toUri();
        log.info("Successfully created Transaction with ID: " + createdTransaction.getId());
        return ResponseEntity.created(locationResource).body(createdTransaction);
    }
}
