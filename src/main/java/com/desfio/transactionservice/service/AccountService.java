package com.desfio.transactionservice.service;

import com.desfio.transactionservice.dto.TransferRequestDTO;

public interface AccountService {

    void transfer(TransferRequestDTO transferRequest);

}
