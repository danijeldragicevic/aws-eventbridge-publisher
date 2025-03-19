package com.productdock.repository;

import com.productdock.exception.OrderRepositoryException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import software.amazon.awssdk.services.eventbridge.EventBridgeClient;
import software.amazon.awssdk.services.eventbridge.model.EventBridgeException;
import software.amazon.awssdk.services.eventbridge.model.PutEventsRequest;
import software.amazon.awssdk.services.eventbridge.model.PutEventsResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class OrderRepositoryTest {

    @Mock
    private EventBridgeClient eventBridgeClient;

    @InjectMocks
    private OrderRepository orderRepository;

    private final String testEventBusName = "test-event-bus";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        orderRepository = new OrderRepository(eventBridgeClient, testEventBusName);
    }

    @Test
    void shouldPublishOrderSuccessfully() {
        // Given (Mock EventBridgeClient Response)
        PutEventsResponse mockResponse = PutEventsResponse.builder().build();
        when(eventBridgeClient.putEvents(any(PutEventsRequest.class))).thenReturn(mockResponse);

        // When
        orderRepository.publishOrder("com.productdock.orders", "OrderPlaced", "{\"orderId\":\"test12\"}");

        // Then
        verify(eventBridgeClient, times(1)).putEvents(any(PutEventsRequest.class));
    }

    @Test
    void shouldThrowOrderRepositoryExceptionWhenEventBridgeFails() {
        // Given (Mock EventBridgeClient Failure)
        when(eventBridgeClient.putEvents(any(PutEventsRequest.class)))
                .thenThrow(EventBridgeException.builder().message("Mock EventBridge error").build());

        // When & Then
        OrderRepositoryException exception =
                assertThrows(OrderRepositoryException.class, () ->
                        orderRepository.publishOrder("com.productdock.orders", "OrderPlaced", "{\"orderId\":\"test12\"}")
                );

        // Verify Exception Message
        assertEquals("Failed to publish order event", exception.getMessage());

        // Ensure EventBridge was called once before failing
        verify(eventBridgeClient, times(1)).putEvents(any(PutEventsRequest.class));
    }
}