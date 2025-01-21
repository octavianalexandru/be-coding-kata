package com.hailo_demo.be_coding_kata.service;

import com.hailo_demo.be_coding_kata.domain.ProductPricingData;
import com.hailo_demo.be_coding_kata.repository.PricingDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PricingDataServiceTest {

    private PricingDataService pricingDataService;

    @Mock
    private PricingDataRepository pricingDataRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        pricingDataService = new PricingDataService(pricingDataRepository);
    }

    @Test
    void testGetProductPricing_ValidBarcode() {
        String barcode = "12345";
        ProductPricingData expectedProduct = ProductPricingData.builder()
                .name("Apple")
                .priceForOne(0.50)
                .build();
        Map<String, ProductPricingData> productPriceMap = Map.of(barcode, expectedProduct);
        when(pricingDataRepository.getProductPriceMap()).thenReturn(productPriceMap);

        ProductPricingData result = pricingDataService.getProductPricing(barcode);

        assertNotNull(result, "ProductPricingData should not be null for a valid barcode");
        assertEquals(expectedProduct, result, "The returned product pricing data should match the expected data");
        verify(pricingDataRepository, times(1)).getProductPriceMap();
    }

    @Test
    void testGetProductPricing_InvalidBarcode() {
        String barcode = "99999";
        Map<String, ProductPricingData> productPriceMap = Map.of(
                "12345", ProductPricingData.builder().name("Apple").priceForOne(0.50).build()
        );
        when(pricingDataRepository.getProductPriceMap()).thenReturn(productPriceMap);

        ProductPricingData result = pricingDataService.getProductPricing(barcode);

        assertNull(result, "ProductPricingData should be null for an invalid barcode");
        verify(pricingDataRepository, times(1)).getProductPriceMap();
    }

    @Test
    void testGetProductPricing_EmptyPriceMap() {
        String barcode = "12345";
        when(pricingDataRepository.getProductPriceMap()).thenReturn(Map.of());

        ProductPricingData result = pricingDataService.getProductPricing(barcode);

        assertNull(result, "ProductPricingData should be null when the price map is empty");
        verify(pricingDataRepository, times(1)).getProductPriceMap();
    }
}
