package com.productdock.exception;

/**
 * Custom exception for handling errors in the order repository.
 */
public class OrderRepositoryException extends RuntimeException {

    /**
     * Constructs a new OrderRepositoryException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause of the exception
     */
    public OrderRepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
