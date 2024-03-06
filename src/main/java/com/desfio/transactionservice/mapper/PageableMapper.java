package com.desfio.transactionservice.mapper;

import com.desfio.transactionservice.dto.PageableResponseDTO;
import org.springframework.data.domain.Page;

public interface PageableMapper {
    PageableResponseDTO toDto(Page<?> page);
}
