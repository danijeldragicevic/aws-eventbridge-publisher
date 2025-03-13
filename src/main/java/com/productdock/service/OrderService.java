package com.productdock.service;

import com.productdock.model.OrderEvent;
import com.productdock.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public  void createOrder(OrderEvent orderEvent) {
        orderRepository.publishOrder(orderEvent);
    }
}
