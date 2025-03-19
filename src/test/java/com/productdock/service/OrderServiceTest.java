package com.productdock.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.productdock.exception.OrderRepositoryException;
import com.productdock.exception.OrderServiceException;
import com.productdock.model.OrderEvent;
import com.productdock.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for OrderService.
 */
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private OrderService orderService;

    private OrderEvent testOrderEvent;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize Test Data
        testOrderEvent = new OrderEvent(
                "com.productdock.orders",
                "OrderPlaced",
                "test12",
                112.1,
                new OrderEvent.Customer("123 Main Street", "AnyCity", "WA", "US")
        );
    }

    /**
     * Tests the createOrder method for successful order creation.
     */
    @Test
    void createOrder_success() throws Exception {
        // Given (Mock JSON Serialization)
        String jsonOrderEvent = "{\"source\":\"com.productdock.orders\",\"detailType\":\"OrderPlaced\",\"orderId\":\"test12\"}";
        when(objectMapper.writeValueAsString(testOrderEvent)).thenReturn(jsonOrderEvent);

        // When
        assertDoesNotThrow(() -> orderService.createOrder(testOrderEvent));

        // Then
        verify(orderRepository, times(1)).publishOrder(
                testOrderEvent.getSource(),
                testOrderEvent.getDetailType(),
                jsonOrderEvent
        );
    }

    /**
     * Tests the createOrder method for JSON processing exception.
     */
    @Test
    void createOrder_jsonProcessingException() throws Exception {
        // Given (Mock ObjectMapper Failure)
        when(objectMapper.writeValueAsString(testOrderEvent)).thenThrow(new JsonProcessingException("Mock JSON error") {});

        // When & Then
        OrderServiceException exception = assertThrows(OrderServiceException.class,
                () -> orderService.createOrder(testOrderEvent));

        assertEquals("Failed to convert order event to JSON", exception.getMessage());
        verify(orderRepository, never()).publishOrder(any(), any(), any());
    }

    /**
     * Tests the createOrder method for repository exception.
     */
    @Test
    void createOrder_repositoryException() throws Exception {
        // Given (Mock JSON Serialization)
        String jsonOrderEvent = "{\"source\":\"com.productdock.orders\",\"detailType\":\"OrderPlaced\",\"orderId\":\"test12\"}";
        when(objectMapper.writeValueAsString(testOrderEvent)).thenReturn(jsonOrderEvent);

        // Given (Mock Repository Failure)
        doThrow(new OrderRepositoryException("Failed to publish order event", new RuntimeException("Mock Repository error")))
                .when(orderRepository).publishOrder(any(), any(), any());

        // When & Then
        OrderRepositoryException exception = assertThrows(OrderRepositoryException.class,
                () -> orderService.createOrder(testOrderEvent));

        assertEquals("Failed to publish order event", exception.getMessage());
        verify(orderRepository, times(1)).publishOrder(any(), any(), any());
    }
}
