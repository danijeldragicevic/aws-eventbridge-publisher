package com.productdock.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.eventbridge.EventBridgeClient;

/**
 * Configuration class for AWS services.
 */
@Configuration
public class AwsConfig {

    @Value("${aws.eventbridge.region}")
    private String region;

    /**
     * Creates an EventBridgeClient bean.
     *
     * The client will be configured with the specified region and will automatically fetch
     * login credentials from the IAM role associated with the EC2 instance.
     *
     * @return EventBridgeClient configured with the specified region and default credentials.
     */
    @Bean
    public EventBridgeClient eventBridgeClient() {
        return EventBridgeClient.builder()
                .region(Region.of(region))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }
}
