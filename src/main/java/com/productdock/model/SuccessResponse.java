package com.productdock.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

/**
 * Model class representing success response.
 */
@AllArgsConstructor
@Data
public class SuccessResponse {
    private Map<String, String> response;
}