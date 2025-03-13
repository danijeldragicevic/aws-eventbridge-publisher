package com.productdock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OrderEventPublisherApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderEventPublisherApplication.class, args);
        System.out.println("OrderEventPublisherApplication started successfully.");
    }
}