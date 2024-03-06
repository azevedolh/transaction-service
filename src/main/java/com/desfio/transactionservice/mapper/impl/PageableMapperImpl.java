package com.desfio.transactionservice.mapper.impl;

import com.desfio.transactionservice.dto.PageableResponseDTO;
import com.desfio.transactionservice.mapper.PageableMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class PageableMapperImpl implements PageableMapper {

    @Override
    public PageableResponseDTO toDto(Page<?> page) {
        return PageableResponseDTO.builder()
                ._limit(page.getPageable().getPageSize())
                ._offset(page.getPageable().getOffset())
                ._pageNumber(page.getNumber() + 1)
                ._pageElements(page.getNumberOfElements())
                ._totalPages(page.getTotalPages())
                ._totalElements(page.getTotalElements())
                ._moreElements(!page.isLast())
                .build();
    }
}
