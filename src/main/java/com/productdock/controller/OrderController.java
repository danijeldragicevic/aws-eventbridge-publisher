package com.productdock.controller;

import com.productdock.model.OrderEvent;
import com.productdock.model.SuccessResponse;
import com.productdock.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * REST controller for handling order-related operations.
 */
@Slf4j
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    /**
     * Constructor to initialize OrderService.
     *
     * @param orderService the service to handle order operations
     */
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Endpoint to create a new order.
     *
     * @param orderEvent the order event data
     * @return ResponseEntity with a success message and HTTP status code
     */
    @PostMapping
    public ResponseEntity<Map<String, String>> createOrder(@RequestBody OrderEvent orderEvent) {
        // Call the service to create an order
        orderService.createOrder(orderEvent);
        log.info("Order event published successfully");

        // Create a success response
        SuccessResponse response = new SuccessResponse(Map.of("message", "Order created successfully"));
        return new ResponseEntity<>(response.getResponse(), HttpStatus.CREATED);
    }
}
