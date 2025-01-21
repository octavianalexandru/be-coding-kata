package com.hailo_demo.be_coding_kata.controller;

import com.hailo_demo.be_coding_kata.domain.dto.CheckoutDTO;
import com.hailo_demo.be_coding_kata.domain.dto.ErrorDTO;
import com.hailo_demo.be_coding_kata.exception.BarcodesForCheckoutListEmpty;
import com.hailo_demo.be_coding_kata.exception.ProductNotRegistered;
import com.hailo_demo.be_coding_kata.service.CheckoutService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CheckoutControllerTest {

    private CheckoutController checkoutController;

    @Mock
    private CheckoutService checkoutService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        checkoutController = new CheckoutController(checkoutService);
    }

    @Test
    void testCheckout_ValidBarcodes() {
        List<String> barcodes = List.of("12345", "67890");
        CheckoutDTO checkoutDTO = new CheckoutDTO(barcodes);
        Double totalPrice = 50.0;
        when(checkoutService.getPrice(barcodes)).thenReturn(totalPrice);

        ResponseEntity<?> response = checkoutController.checkout(checkoutDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(totalPrice, response.getBody());
        verify(checkoutService, times(1)).getPrice(barcodes);
    }

    @Test
    void testCheckout_EmptyBarcodesList() {
        List<String> barcodes = new ArrayList<>();
        CheckoutDTO checkoutDTO = new CheckoutDTO(barcodes);
        when(checkoutService.getPrice(barcodes)).thenThrow(new BarcodesForCheckoutListEmpty());

        ResponseEntity<?> response = checkoutController.checkout(checkoutDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorDTO errorDTO = (ErrorDTO) response.getBody();
        assertEquals("No product sent for price calculation", errorDTO.getErrorMessage());
        assertEquals("haiilo.noProduct", errorDTO.getErrorKey());
        verify(checkoutService, times(1)).getPrice(barcodes);
    }

    @Test
    void testCheckout_ProductNotRegistered() {
        List<String> barcodes = List.of("12345", "99999");
        CheckoutDTO checkoutDTO = new CheckoutDTO(barcodes);
        when(checkoutService.getPrice(barcodes)).thenThrow(new ProductNotRegistered());

        ResponseEntity<?> response = checkoutController.checkout(checkoutDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorDTO errorDTO = (ErrorDTO) response.getBody();
        assertEquals("One of the barcodes is missing from our pricing list", errorDTO.getErrorMessage());
        assertEquals("haiilo.productNotRegistered", errorDTO.getErrorKey());
        verify(checkoutService, times(1)).getPrice(barcodes);
    }

    @Test
    void testCheckout_UnexpectedException() {
        List<String> barcodes = List.of("12345", "67890");
        CheckoutDTO checkoutDTO = new CheckoutDTO(barcodes);
        when(checkoutService.getPrice(barcodes)).thenThrow(new RuntimeException("Unexpected error"));

        ResponseEntity<?> response = checkoutController.checkout(checkoutDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        ErrorDTO errorDTO = (ErrorDTO) response.getBody();
        assertEquals("Unkown error", errorDTO.getErrorMessage());
        assertEquals("haiilo.unkownError", errorDTO.getErrorKey());
        verify(checkoutService, times(1)).getPrice(barcodes);
    }
}
