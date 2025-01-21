package com.hailo_demo.be_coding_kata.exception;

/**
 * Exception thrown when the list of barcodes is empty on a checkout request.
 */
public class BarcodesForCheckoutListEmpty extends RuntimeException{
    public BarcodesForCheckoutListEmpty() {
        super("Product list for calculating price is empty.");
    }

    public BarcodesForCheckoutListEmpty(String message) {
        super(message);
    }
}
