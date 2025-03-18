package com.productdock.exception;

import com.productdock.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@Slf4j
@ControllerAdvice
public class _GlobalExceptionHandler {

    @ExceptionHandler(OrderRepositoryException.class)
    public ResponseEntity<ErrorResponse> handleOrderRepositoryException(OrderRepositoryException exception, WebRequest request) {
        log.error("Handling OrderRepositoryException: {}", exception.getMessage());
        return buildErrorResponse(exception.getMessage(), HttpStatus.SERVICE_UNAVAILABLE, request);
    }

    @ExceptionHandler(OrderServiceException.class)
    public ResponseEntity<ErrorResponse> handleOrderServiceException(OrderServiceException exception, WebRequest request) {
        log.error("Handling OrderServiceException: {}", exception.getMessage());
        return buildErrorResponse(exception.getMessage(), HttpStatus.BAD_REQUEST, request);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(String message, HttpStatus httpStatus, WebRequest request) {
        String requestURI = (((ServletWebRequest) request).getRequest().getRequestURI());

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                httpStatus.value(),
                message,
                requestURI
        );

        return new ResponseEntity<>(errorResponse, httpStatus);
    }
}
