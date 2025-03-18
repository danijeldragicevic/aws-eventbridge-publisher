package com.productdock.model;

import lombok.*;

@AllArgsConstructor
@Data
public class  OrderEvent {
    @NonNull
    String source;
    @NonNull
    String detailType;
    @NonNull
    String orderId;
    @NonNull
    Double orderAmount;
    @NonNull
    Customer customer;

    @AllArgsConstructor
    @Data
    public static class Customer {
        @NonNull
        String address;
        @NonNull
        String city;
        @NonNull
        String state;
        @NonNull
        String countryCode;
    }
}
