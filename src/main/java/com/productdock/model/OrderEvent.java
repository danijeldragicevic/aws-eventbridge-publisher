package com.productdock.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderEvent {
    private String source;
    private String detailType;
    private String orderId;
    private double orderAmount;
    private Customer customer;

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Customer {
        private String address;
        private String city;
        private String state;
        private String countryCode;
    }
}
