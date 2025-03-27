package com.productdock.repository;

import com.productdock.exception.OrderRepositoryException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.eventbridge.EventBridgeClient;
import software.amazon.awssdk.services.eventbridge.model.EventBridgeException;
import software.amazon.awssdk.services.eventbridge.model.PutEventsRequest;
import software.amazon.awssdk.services.eventbridge.model.PutEventsRequestEntry;

/**
 * Repository class for handling order-related operations with AWS EventBridge.
 */
@Slf4j
@Repository
public class OrderRepository {

    private final EventBridgeClient eventBridgeClient;
    private final String eventBusName;

    /**
     * Constructs an OrderRepository with the specified EventBridge client and event bus name.
     *
     * @param eventBridgeClient the EventBridge client
     * @param eventBusName the name of the event bus
     */
    public OrderRepository(
                EventBridgeClient eventBridgeClient,
                @Value("${aws.eventbridge.event-bus-name}") String eventBusName) {
        this.eventBridgeClient = eventBridgeClient;
        this.eventBusName = eventBusName;
    }

    /**
     * Publishes an order event to AWS EventBridge.
     *
     * @param eventSource the source of the event
     * @param detailType the type of the event detail
     * @param eventDetail the detail of the event
     * @throws OrderRepositoryException if an error occurs while publishing the event
     */
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
