package com.productdock.config;

import org.springframework.context.annotation.Bean;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.eventbridge.EventBridgeClient;

public class AwsConfig {

    @Bean
    public EventBridgeClient eventBridgeClient() {
        return EventBridgeClient.builder()
                .region(Region.EU_NORTH_1)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }
}
