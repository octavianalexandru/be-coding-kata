package com.hailo_demo.be_coding_kata.exception;

public class BarcodesForCheckoutListEmpty extends RuntimeException{
    public BarcodesForCheckoutListEmpty() {
        super("Product list for calculating price is empty.");
    }

    public BarcodesForCheckoutListEmpty(String message) {
        super(message);
    }
}
