package com.desfio.transactionservice.exception;

import com.desfio.transactionservice.dto.ApiErrorResponseDTO;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler({CustomBusinessException.class})
    public ResponseEntity<ApiErrorResponseDTO> handleCustomException(CustomBusinessException ex, WebRequest request) {
        ApiErrorResponseDTO apiErrorResponseDTO = new ApiErrorResponseDTO(
                ex.getHttpStatus() == null ? HttpStatus.BAD_REQUEST : ex.getHttpStatus(),
                1000,
                "ERROR",
                ex.getLocalizedMessage(),
                ex.getDetails()
        );

        return new ResponseEntity<>(apiErrorResponseDTO, new HttpHeaders(), apiErrorResponseDTO.getHttpStatus());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ApiErrorResponseDTO> handleException(MethodArgumentNotValidException ex, WebRequest request) {
        ApiErrorResponseDTO apiErrorResponseDTO = new ApiErrorResponseDTO(
                (HttpStatus) ex.getStatusCode(),
                2000,
                "ERROR",
                ex.getBody().getDetail(),
                ex.getDetailMessageArguments()
        );

        return new ResponseEntity<>(apiErrorResponseDTO, new HttpHeaders(), apiErrorResponseDTO.getHttpStatus());
    };

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ApiErrorResponseDTO> handleException(MethodArgumentTypeMismatchException ex, WebRequest request) {
        ApiErrorResponseDTO apiErrorResponseDTO = new ApiErrorResponseDTO(
                HttpStatus.BAD_REQUEST,
                3000,
                "ERROR",
                ex.getLocalizedMessage(),
                ex.getPropertyName()
        );

        return new ResponseEntity<>(apiErrorResponseDTO, new HttpHeaders(), apiErrorResponseDTO.getHttpStatus());
    };

    @ExceptionHandler({MissingServletRequestParameterException.class})
    public ResponseEntity<ApiErrorResponseDTO> handleException(MissingServletRequestParameterException ex, WebRequest request) {
        ApiErrorResponseDTO apiErrorResponseDTO = new ApiErrorResponseDTO(
                HttpStatus.BAD_REQUEST,
                4000,
                "ERROR",
                ex.getLocalizedMessage(),
                ex.getParameterName()
        );

        return new ResponseEntity<>(apiErrorResponseDTO, new HttpHeaders(), apiErrorResponseDTO.getHttpStatus());
    };


    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<ApiErrorResponseDTO> handleException(HttpMessageNotReadableException ex, WebRequest request) {
        ApiErrorResponseDTO apiErrorResponseDTO = new ApiErrorResponseDTO(
                HttpStatus.BAD_REQUEST,
                5000,
                "ERROR",
                ex.getLocalizedMessage(),
                ex.getLocalizedMessage()
        );

        return new ResponseEntity<>(apiErrorResponseDTO, new HttpHeaders(), apiErrorResponseDTO.getHttpStatus());
    };
}
