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

@Slf4j
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
        log.info("Order event published successfully");

        SuccessResponse response = new SuccessResponse(Map.of("message", "Order created successfully"));
        return new ResponseEntity<>(response.getResponse(), HttpStatus.CREATED);
    }
}
