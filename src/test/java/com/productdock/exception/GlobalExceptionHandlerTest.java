package com.productdock.exception;

import com.productdock.model.ErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test class for GlobalExceptionHandler.
 */
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;
    private WebRequest mockRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        exceptionHandler = new GlobalExceptionHandler();
        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        servletRequest.setRequestURI("/orders");
        mockRequest = new ServletWebRequest(servletRequest);
    }

    @Test
    void shouldHandleOrderRepositoryException() {
        // Given
        OrderRepositoryException exception = new OrderRepositoryException("Failed to publish order event", new RuntimeException("Mock EventBridge error"));

        // When
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleOrderRepositoryException(exception, mockRequest);

        // Then
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Failed to publish order event", response.getBody().getError());
        assertEquals("/orders", response.getBody().getPath());
    }

    @Test
    void shouldHandleOrderServiceException() {
        // Given
        OrderServiceException exception = new OrderServiceException("Failed to process order", new RuntimeException("Mock JSON error"));

        // When
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleOrderServiceException(exception, mockRequest);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Failed to process order", response.getBody().getError());
        assertEquals("/orders", response.getBody().getPath());
    }
}
