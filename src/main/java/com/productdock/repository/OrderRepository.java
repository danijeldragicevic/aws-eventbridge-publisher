package com.productdock.repository;

import com.productdock.exception.AwsServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.eventbridge.EventBridgeClient;
import software.amazon.awssdk.services.eventbridge.model.EventBridgeException;
import software.amazon.awssdk.services.eventbridge.model.PutEventsRequest;
import software.amazon.awssdk.services.eventbridge.model.PutEventsRequestEntry;

@Slf4j
@Repository
public class OrderRepository {

    private final EventBridgeClient eventBridgeClient;

    public OrderRepository(EventBridgeClient eventBridgeClient) {
        this.eventBridgeClient = eventBridgeClient;
    }

    public void publishOrder(String eventSource, String detailType, String eventDetail) {
        try {
            PutEventsRequestEntry eventEntry = PutEventsRequestEntry.builder()
                    .eventBusName("Orders") //TODO move to configuration file
                    .source(eventSource)
                    .detailType(detailType)
                    .detail(eventDetail)
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
