package com.desfio.transactionservice.service.impl;

import com.desfio.transactionservice.dto.ApiErrorResponseDTO;
import com.desfio.transactionservice.dto.StatusResponseDTO;
import com.desfio.transactionservice.dto.TransferRequestDTO;
import com.desfio.transactionservice.exception.CustomBusinessException;
import com.desfio.transactionservice.service.AccountService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Log4j2
@Service
public class AccountServiceImpl implements AccountService {

    private RestTemplate restTemplate;

    @Value("${account.service.url}")
    private String ACCOUNT_SERVICE_URL;

    @Autowired
    public AccountServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void transfer(TransferRequestDTO transferRequest) throws CustomBusinessException {

        ResponseEntity<StatusResponseDTO> response = null;

        try {
            response = restTemplate.postForEntity(
                    ACCOUNT_SERVICE_URL,
                    transferRequest,
                    StatusResponseDTO.class
            );
        } catch (HttpClientErrorException e) {
            ApiErrorResponseDTO errorResponse = e.getResponseBodyAs(ApiErrorResponseDTO.class);

            throw new CustomBusinessException(
                errorResponse != null ? errorResponse.getHttpStatus() : HttpStatus.INTERNAL_SERVER_ERROR,
                errorResponse != null ? errorResponse.getMessage() : "Erro na comunicação com serviço de Contas",
                errorResponse != null ? errorResponse.getDetails() : ""
            );
        }
    }
}
