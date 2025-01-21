package com.hailo_demo.be_coding_kata.exception;

/**
 * Exception thrown when a product that is not present in our pricing list is sent to checkout.
 */
public class ProductNotRegistered extends RuntimeException{
    public ProductNotRegistered() {
        super("Product is not registered.");
    }

    public ProductNotRegistered(String message) {
        super(message);
    }
}
