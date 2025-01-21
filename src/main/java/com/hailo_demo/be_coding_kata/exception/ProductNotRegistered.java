package com.hailo_demo.be_coding_kata.exception;

public class ProductNotRegistered extends RuntimeException{
    public ProductNotRegistered() {
        super("Product is not registered.");
    }

    public ProductNotRegistered(String message) {
        super(message);
    }
}
