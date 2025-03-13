package com.productdock.service;

import com.productdock.model.OrderEvent;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    public  void createOrder(OrderEvent orderEvent) {
        System.out.println("Order created: " + orderEvent);
    }
}
