package com.productdock.exception;

/**
 * Custom exception for handling errors in the order service.
 */
public class OrderServiceException extends RuntimeException {

    /**
     * Constructs a new OrderServiceException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause of the exception
     */
    public OrderServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
