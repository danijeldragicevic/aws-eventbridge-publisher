package com.productdock.controller;

import com.productdock.model.OrderEvent;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @PostMapping
    public void createOrder(@RequestBody OrderEvent orderEvent) {
        System.out.println("Order created:\n" + orderEvent);
    }
}
