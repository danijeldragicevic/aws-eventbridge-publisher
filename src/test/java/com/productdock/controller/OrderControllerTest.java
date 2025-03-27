package com.productdock.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.productdock.exception.GlobalExceptionHandler;
import com.productdock.exception.OrderRepositoryException;
import com.productdock.model.OrderEvent;
import com.productdock.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for OrderController.
 */
public class OrderControllerTest {
    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private OrderEvent testOrderEvent;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(orderController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        testOrderEvent = new OrderEvent(
                "com.productdock.orders",
                "OrderPlaced",
                "test12",
                112.1,
                new OrderEvent.Customer("123 Main Street", "AnyCity", "WA", "US")
        );
    }

    /**
     * Tests the createOrder endpoint for successful order creation.
     */
    @Test
    void createOrder_success() throws Exception {
        // When (Mocking Service Call)
        doNothing().when(orderService).createOrder(any(OrderEvent.class));

        // Then (Perform POST Request and Validate Response)
        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testOrderEvent)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Order created successfully"))
                .andDo(print());

        // Verify that the service was called once
        verify(orderService, times(1)).createOrder(any(OrderEvent.class));
    }

    /**
     * Tests the createOrder endpoint for service failure.
     */
    @Test
    void createOrder_serviceFailure() throws Exception {
        // When (Mock Service to Throw Exception)
        doThrow(new OrderRepositoryException("Failed to publish order event", new RuntimeException("Simulated Error")))
                .when(orderService).createOrder(any(OrderEvent.class));

        // Then (Perform POST Request & Validate Error Response)
        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testOrderEvent)))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.error").value("Failed to publish order event"))
                .andDo(print());

        // Verify the service was called
        verify(orderService, times(1)).createOrder(any(OrderEvent.class));
    }
}
