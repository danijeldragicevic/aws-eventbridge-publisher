package com.productdock.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.productdock.exception.AwsServiceException;
import com.productdock.model.OrderEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.eventbridge.EventBridgeClient;
import software.amazon.awssdk.services.eventbridge.model.EventBridgeException;
import software.amazon.awssdk.services.eventbridge.model.PutEventsRequest;
import software.amazon.awssdk.services.eventbridge.model.PutEventsRequestEntry;
import software.amazon.awssdk.services.eventbridge.model.PutEventsResponse;

@Slf4j
@Repository
public class OrderRepository {

    private final EventBridgeClient eventBridgeClient;

    public OrderRepository(EventBridgeClient eventBridgeClient) {
        this.eventBridgeClient = eventBridgeClient;
    }

    public void publishOrder(OrderEvent orderEvent) {
        ObjectMapper objectMapper = new ObjectMapper();
        String orderEventAsString;

        try {
            orderEventAsString = objectMapper.writeValueAsString(orderEvent);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize order event", e);
            throw new RuntimeException(e);
        }

        try {
            PutEventsRequestEntry eventEntry = PutEventsRequestEntry.builder()
                    .eventBusName("Orders")
                    .source(orderEvent.getSource())
                    .detailType(orderEvent.getDetailType())
                    .detail(orderEventAsString)
                    .build();

            PutEventsRequest request = PutEventsRequest.builder()
                    .entries(eventEntry)
                    .build();

            eventBridgeClient.putEvents(request);
            log.info("Order event published successfully");

        } catch (EventBridgeException e) {
            throw new AwsServiceException("Failed to publish order event", e);
        }
    }
}
