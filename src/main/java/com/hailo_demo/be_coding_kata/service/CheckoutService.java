package com.hailo_demo.be_coding_kata.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hailo_demo.be_coding_kata.controller.CheckoutController;
import com.hailo_demo.be_coding_kata.domain.Offer;
import com.hailo_demo.be_coding_kata.domain.ProductPricingData;
import com.hailo_demo.be_coding_kata.repository.PricingDataRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CheckoutService {
    private final PricingDataRepository pricingDataRepository;

    public ProductPricingData getProductPricing(String barcode) {
        return pricingDataRepository.getProductPriceMap().get(barcode);
    }
}
