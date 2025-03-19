package com.productdock.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.productdock.exception.OrderRepositoryException;
import com.productdock.exception.OrderServiceException;
import com.productdock.model.OrderEvent;
import com.productdock.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service class for handling order-related operations.
 */
@Slf4j
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ObjectMapper objectMapper;

    /**
     * Constructs an OrderService with the specified OrderRepository and ObjectMapper.
     *
     * @param orderRepository the repository for order operations
     * @param objectMapper the object mapper for JSON processing
     */
    public OrderService(OrderRepository orderRepository, ObjectMapper objectMapper) {
        this.orderRepository = orderRepository;
        this.objectMapper = objectMapper;
    }

    /**
     * Creates an order by converting the order event to JSON and publishing it.
     *
     * @param orderEvent the order event to be created
     * @throws OrderServiceException if an error occurs while converting the order event to JSON
     * @throws OrderRepositoryException if an error occurs while publishing the order event
     */
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
