package com.productdock.repository;

import com.productdock.model.OrderEvent;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.eventbridge.EventBridgeClient;

@Repository
public class OrderRepository {

    private final EventBridgeClient eventBridgeClient;

    public OrderRepository(EventBridgeClient eventBridgeClient) {
        this.eventBridgeClient = eventBridgeClient;
    }

    public void publishOrder(OrderEvent orderEvent) {
        System.out.println("Order published: " + orderEvent);
        System.out.println(eventBridgeClient);
    }
}
