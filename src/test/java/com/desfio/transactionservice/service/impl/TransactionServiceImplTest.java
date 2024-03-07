package com.desfio.transactionservice.service.impl;

import com.desfio.transactionservice.dto.CreateTransactionRequestDTO;
import com.desfio.transactionservice.dto.PageResponseDTO;
import com.desfio.transactionservice.dto.TransactionResponseDTO;
import com.desfio.transactionservice.dto.TransferRequestDTO;
import com.desfio.transactionservice.exception.CustomBusinessException;
import com.desfio.transactionservice.mapper.TransactionRequestMapperImpl;
import com.desfio.transactionservice.mapper.impl.PageableMapperImpl;
import com.desfio.transactionservice.mapper.impl.TransactionResponseMapperImpl;
import com.desfio.transactionservice.model.Transaction;
import com.desfio.transactionservice.producer.NotificationProducer;
import com.desfio.transactionservice.repository.TransactionRepository;
import com.desfio.transactionservice.service.AccountService;
import com.desfio.transactionservice.util.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Captor
    private ArgumentCaptor<Transaction> transactionCaptor;

    @Mock
    private TransactionRepository repository;

    @Spy
    private PageableMapperImpl pageableMapper;

    @Spy
    private TransactionResponseMapperImpl transactionResponseMapper;

    @Spy
    private TransactionRequestMapperImpl transactionRequestMapper;

    @Mock
    private AccountService accountService;

    @Mock
    private NotificationProducer notificationProducer;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Test
    void testShouldReturnAllTransactionsWhenInvoked() {
        PageRequest pageRequest = PageRequest.of(1, 10);
        Page<Transaction> expectedReturn = generatePage(pageRequest);


        when(repository.findTransactionsByAgencyAndAccount(any(PageRequest.class), anyString(), anyLong()))
                .thenReturn(expectedReturn);

        PageResponseDTO<TransactionResponseDTO> transactions = transactionService.getTransactions(
                "1234",
                1L,
                1,
                10,
                null
        );

        verify(repository).findTransactionsByAgencyAndAccount(any(PageRequest.class), anyString(), anyLong());

        assertEquals(expectedReturn.getContent().size(),
                transactions.get_content().size(),
                "Should be equal");

        assertEquals(expectedReturn.getContent().get(0).getId(),
                transactions.get_content().get(0).getId(),
                "Should be equal");

        assertEquals(expectedReturn.getContent().get(0).getOriginCustomerId(),
                transactions.get_content().get(0).getOriginCustomerId(),
                "Should be equal");

        assertEquals(expectedReturn.getContent().get(0).getOriginAgency(),
                transactions.get_content().get(0).getOriginAgency(),
                "Should be equal");

        assertEquals(expectedReturn.getContent().get(0).getOriginAccount(),
                transactions.get_content().get(0).getOriginAccount(),
                "Should be equal");

        assertEquals(expectedReturn.getContent().get(0).getDestinationCustomerId(),
                transactions.get_content().get(0).getDestinationCustomerId(),
                "Should be equal");

        assertEquals(expectedReturn.getContent().get(0).getDestinationAgency(),
                transactions.get_content().get(0).getDestinationAgency(),
                "Should be equal");

        assertEquals(expectedReturn.getContent().get(0).getDestinationAccount(),
                transactions.get_content().get(0).getDestinationAccount(),
                "Should be equal");

        assertEquals(expectedReturn.getContent().get(0).getAmount(),
                transactions.get_content().get(0).getAmount(),
                "Should be equal");

        assertEquals(expectedReturn.getContent().get(0).getCreatedAt(),
                transactions.get_content().get(0).getCreatedAt(),
                "Should be equal");

        assertEquals(expectedReturn.getContent().get(0).getUpdatedAt(),
                transactions.get_content().get(0).getUpdatedAt(),
                "Should be equal");
    }

    @Test
    void testShouldReturnNullContentWhenPageObjectNull() {
        when(repository.findTransactionsByAgencyAndAccount(any(PageRequest.class), anyString(), anyLong()))
                .thenReturn(null);

        PageResponseDTO<TransactionResponseDTO> transactions = transactionService.getTransactions(
                "1234",
                1L,
                1,
                10,
                null
        );

        verify(repository).findTransactionsByAgencyAndAccount(any(PageRequest.class), anyString(), anyLong());

        assertNull(transactions.get_content(), "Should be null");
    }

    private Page<Transaction> generatePage(PageRequest pageRequest) {

        List<Transaction> transactionList = TestUtils.generateListOfTransactions();

        return new PageImpl<>(transactionList, pageRequest, transactionList.size());
    }

    @Test
    void testShouldCreateTransactionAndGetPositiveReturnFromSentNotificationWhenMethodIsInvoked() {
        Transaction expected = TestUtils.generateATransaction(
                "1234",
                1L,
                "1235",
                2L,
                new BigDecimal(100)
        );

        CreateTransactionRequestDTO requestDTO = CreateTransactionRequestDTO.builder()
                .originCustomerId(expected.getOriginCustomerId())
                .originAgency("1234")
                .originAccount(1L)
                .destinationCustomerId(expected.getDestinationCustomerId())
                .destinationAgency("1235")
                .destinationAccount(2L)
                .amount(new BigDecimal(100))
                .build();

        when(repository.saveAndFlush(any(Transaction.class))).thenReturn(expected);
        TransactionResponseDTO responseDTO = transactionService.create(requestDTO);

        verify(repository).saveAndFlush(any());

        assertEquals(expected.getId(), responseDTO.getId(), "Should be equal");
        assertEquals(expected.getOriginAgency(), responseDTO.getOriginAgency(), "Should be equal");
        assertEquals(expected.getOriginCustomerId(), responseDTO.getOriginCustomerId(), "Should be equal");
        assertEquals(expected.getOriginAccount(), responseDTO.getOriginAccount(), "Should be equal");
        assertEquals(requestDTO.getDestinationCustomerId(), responseDTO.getDestinationCustomerId(), "Should be equal");
        assertEquals(requestDTO.getDestinationAgency(), responseDTO.getDestinationAgency(), "Should be equal");
        assertEquals(requestDTO.getDestinationAccount(), responseDTO.getDestinationAccount(), "Should be equal");
        assertEquals(requestDTO.getAmount(), responseDTO.getAmount(), "Should be equal");
    }

    @Test
    void testShouldThrowExceptionWhenTryingToMakeATransactionWithSameAccountAsOriginAndDestination() {
        Transaction expected = TestUtils.generateATransaction(
                "1234",
                1L,
                "1235",
                2L,
                new BigDecimal(100)
        );

        CreateTransactionRequestDTO requestDTO = CreateTransactionRequestDTO.builder()
                .originCustomerId(expected.getOriginCustomerId())
                .originAgency("1234")
                .originAccount(1L)
                .destinationCustomerId(expected.getDestinationCustomerId())
                .destinationAgency("1234")
                .destinationAccount(1L)
                .amount(new BigDecimal(100))
                .build();

        CustomBusinessException exception = assertThrows(
                CustomBusinessException.class,
                () -> transactionService.create(requestDTO),
                "Should throw an exception");

        assertTrue(exception.getMessage().contains("Não é possível realizar o pagamento"),
                "Should be true");
    }

    @Test
    void testShouldThrowExceptionWhenCantTransfer() {
        Transaction expected = TestUtils.generateATransaction(
                "1234",
                1L,
                "1235",
                2L,
                new BigDecimal(100)
        );

        CreateTransactionRequestDTO requestDTO = CreateTransactionRequestDTO.builder()
                .destinationAccount(2L)
                .amount(new BigDecimal(100))
                .build();

        doThrow(new CustomBusinessException("Erro ao realizar processo de atualização de saldo"))
                .when(accountService).transfer(any(TransferRequestDTO.class));

        CustomBusinessException exception = assertThrows(
                CustomBusinessException.class,
                () -> transactionService.create(requestDTO),
                "Should throw an exception");

        assertTrue(exception.getMessage().contains("Erro ao realizar processo de atualização de saldo"),
                "Should be true");
    }
}