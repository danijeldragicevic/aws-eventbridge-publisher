package com.productdock.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@AllArgsConstructor
@Data
public class SuccessResponse {
    private Map<String, String> response;
}