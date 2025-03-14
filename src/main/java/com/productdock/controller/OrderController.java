package com.productdock.controller;

import com.productdock.model.OrderEvent;
import com.productdock.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> createOrder(@RequestBody OrderEvent orderEvent) {
        orderService.createOrder(orderEvent);

        Map<String, String> response = Map.of("message", "Order created successfully");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
