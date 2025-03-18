package com.productdock.repository;

import com.productdock.exception.OrderRepositoryException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.eventbridge.EventBridgeClient;
import software.amazon.awssdk.services.eventbridge.model.EventBridgeException;
import software.amazon.awssdk.services.eventbridge.model.PutEventsRequest;
import software.amazon.awssdk.services.eventbridge.model.PutEventsRequestEntry;

@Slf4j
@Repository
public class OrderRepository {

    private final EventBridgeClient eventBridgeClient;
    private final String eventBusName;

    public OrderRepository(
                EventBridgeClient eventBridgeClient,
                @Value("${aws.eventbridge.event-bus-name}") String eventBusName) {
        this.eventBridgeClient = eventBridgeClient;
        this.eventBusName = eventBusName;
    }

    public void publishOrder(String eventSource, String detailType, String eventDetail) throws OrderRepositoryException {
        try {
            PutEventsRequestEntry eventEntry = PutEventsRequestEntry.builder()
                    .eventBusName(eventBusName)
                    .source(eventSource)
                    .detailType(detailType)
                    .detail(eventDetail)
                    .build();

            PutEventsRequest request = PutEventsRequest.builder()
                    .entries(eventEntry)
                    .build();

            eventBridgeClient.putEvents(request);

        } catch (EventBridgeException e) {
            log.error("Error occurred in repository layer: {}", e.getMessage());
            throw new OrderRepositoryException("Failed to publish order event", e);
        }
    }
}
