package com.productdock;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class for the Order Event Publisher application.
 */
@Slf4j
@SpringBootApplication
public class OrderEventPublisherApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderEventPublisherApplication.class, args);
        log.info("OrderEventPublisherApplication started successfully.");
    }
}