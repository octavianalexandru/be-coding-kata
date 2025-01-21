package com.hailo_demo.be_coding_kata.service;

import com.hailo_demo.be_coding_kata.domain.Offer;
import com.hailo_demo.be_coding_kata.domain.ProductPricingData;
import com.hailo_demo.be_coding_kata.exception.BarcodesForCheckoutListEmpty;
import com.hailo_demo.be_coding_kata.exception.ProductNotRegistered;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class CheckoutServiceTest {
    private CheckoutService checkoutService;

    @Mock
    private PricingDataService pricingDataService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        checkoutService = new CheckoutService(pricingDataService);
    }

    @Test
    void testGetPrice_NoOffer() {
        ArrayList<String> barCodes = new ArrayList<>();
        barCodes.add("12345");
        barCodes.add("12345");
        ProductPricingData product = ProductPricingData.builder()
                .name("Apple")
                .priceForOne(0.50)
                .build();
        when(pricingDataService.getProductPricing("12345")).thenReturn(product);

        Double totalPrice = checkoutService.getPrice(barCodes);

        assertEquals(1.00, totalPrice, "Total price should be 1.00 for 2 items at 0.50 each");
        verify(pricingDataService, times(1)).getProductPricing("12345");
    }

    @Test
    void testGetPrice_WithOffer() {
        ArrayList<String> barCodes = new ArrayList<>();
        barCodes.add("12345");
        barCodes.add("12345");
        barCodes.add("12345");
        barCodes.add("12345");
        Offer offer = Offer.builder()
                .numberOfItemsForOffer(3)
                .sumOfItemsOffer(1.25)
                .build();
        ProductPricingData product = ProductPricingData.builder()
                .name("Apple")
                .priceForOne(0.50)
                .offer(offer)
                .build();
        when(pricingDataService.getProductPricing("12345")).thenReturn(product);

        Double totalPrice = checkoutService.getPrice(barCodes);

        assertEquals(1.75, totalPrice, "Total price should be 1.25 for 3 items at the offer price and 1 item at regular price");
        verify(pricingDataService, times(1)).getProductPricing("12345");
    }

    @Test
    void testGetPrice_WithOffer_InsufficientItems() {
        ArrayList<String> barCodes = new ArrayList<>();
        barCodes.add("12345");
        barCodes.add("12345");
        Offer offer = Offer.builder()
                .numberOfItemsForOffer(3)
                .sumOfItemsOffer(1.25)
                .build();
        ProductPricingData product = ProductPricingData.builder()
                .name("Apple")
                .priceForOne(0.50)
                .offer(offer)
                .build();
        when(pricingDataService.getProductPricing("12345")).thenReturn(product);

        Double totalPrice = checkoutService.getPrice(barCodes);

        assertEquals(1.00, totalPrice, "Total price should be 1.00 as the offer is not applicable for 2 items");
        verify(pricingDataService, times(1)).getProductPricing("12345");
    }

    @Test
    void testGetPrice_MultipleProducts() {
        ArrayList<String> barCodes = new ArrayList<>();
        barCodes.add("12345");
        barCodes.add("67890");
        ProductPricingData apple = ProductPricingData.builder()
                .name("Apple")
                .priceForOne(0.50)
                .build();
        ProductPricingData orange = ProductPricingData.builder()
                .name("Orange")
                .priceForOne(0.75)
                .build();
        when(pricingDataService.getProductPricing("12345")).thenReturn(apple);
        when(pricingDataService.getProductPricing("67890")).thenReturn(orange);

        Double totalPrice = checkoutService.getPrice(barCodes);

        assertEquals(1.25, totalPrice, "Total price should be 1.25 for 1 apple and 1 orange");
        verify(pricingDataService, times(1)).getProductPricing("12345");
        verify(pricingDataService, times(1)).getProductPricing("67890");
    }

    @Test
    void testGetPrice_EmptyBarcodeList() {
        ArrayList<String> barCodes = new ArrayList<>();

        assertThrows(BarcodesForCheckoutListEmpty.class, () -> {
            checkoutService.getPrice(barCodes);
        });
    }

    @Test
    void testGetPrice_ProductNotRegistered() {
        ArrayList<String> barCodes = new ArrayList<>();
        barCodes.add("99999");
        when(pricingDataService.getProductPricing("99999")).thenReturn(null);

        assertThrows(ProductNotRegistered.class, () -> {
            checkoutService.getPrice(barCodes);
        });
        verify(pricingDataService, times(1)).getProductPricing("99999");
    }
}
