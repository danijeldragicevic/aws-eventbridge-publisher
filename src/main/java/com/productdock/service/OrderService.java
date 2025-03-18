package com.productdock.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.productdock.exception.OrderRepositoryException;
import com.productdock.exception.OrderServiceException;
import com.productdock.model.OrderEvent;
import com.productdock.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ObjectMapper objectMapper;

    public OrderService(OrderRepository orderRepository, ObjectMapper objectMapper) {
        this.orderRepository = orderRepository;
        this.objectMapper = objectMapper;
    }

    public  void createOrder(OrderEvent orderEvent) throws OrderServiceException, OrderRepositoryException {
        String orderEventAsString;

        try {
            orderEventAsString = objectMapper.writeValueAsString(orderEvent);
        } catch (JsonProcessingException e) {
            log.error("Error occurred in service layer: {}", e.getMessage());
            throw new OrderServiceException("Failed to convert order event to JSON", e);
        }

        orderRepository.publishOrder(orderEvent.getSource(), orderEvent.getDetailType(), orderEventAsString);
    }
}
