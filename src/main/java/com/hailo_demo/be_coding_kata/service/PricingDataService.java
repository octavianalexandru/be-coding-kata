package com.hailo_demo.be_coding_kata.service;

import com.hailo_demo.be_coding_kata.domain.ProductPricingData;
import com.hailo_demo.be_coding_kata.repository.PricingDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
@Slf4j
@Service
@RequiredArgsConstructor
public class PricingDataService {
    private final PricingDataRepository pricingDataRepository;

    public ProductPricingData getProductPricing(String barcode) {
        return pricingDataRepository.getProductPriceMap().get(barcode);
    }
}
