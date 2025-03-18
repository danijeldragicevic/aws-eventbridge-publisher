package com.productdock.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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

public class OrderControllerTest {
    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @Test
    void shouldCreateOrderAndReturnSuccessResponse() throws Exception {
        // Given (Test Data)
        OrderEvent orderEvent = new OrderEvent(
                "com.productdock.orders",
                "OrderPlaced",
                "test12",
                112.1,
                new OrderEvent.Customer("123 Main Street", "AnyCity", "WA", "US")
        );

        // When (Mocking Service Call)
        doNothing().when(orderService).createOrder(any(OrderEvent.class));

        // Then (Perform POST Request and Validate Response)
        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderEvent)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Order created successfully"))
                .andDo(print());

        // Verify that the service was called once
        verify(orderService, times(1)).createOrder(any(OrderEvent.class));
    }

    @Test
    void shouldReturnBadRequestWhenInvalidPayload() throws Exception {
        // Given (Invalid JSON: Missing required fields)
        String invalidJson = "{ \"source\": \"com.productdock.orders\" }";

        // Then (Perform POST Request and Expect 400 Bad Request)
        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}
