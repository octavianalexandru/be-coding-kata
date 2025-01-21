package com.hailo_demo.be_coding_kata.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hailo_demo.be_coding_kata.controller.CheckoutController;
import com.hailo_demo.be_coding_kata.domain.Offer;
import com.hailo_demo.be_coding_kata.domain.ProductPricingData;
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
@AllArgsConstructor
public class CheckoutService {


    private final Resource pricingTableResource;
    private final ObjectMapper objectMapper;

    private Map<String, ProductPricingData> productPriceMap = new HashMap<>();


    @PostConstruct
    public void init() {
        try {
            log.info("Pricing table file path: {}", pricingTableResource.getFile().getAbsolutePath());
            loadPricingTable();
        } catch (IOException e) {
            log.error("Failed to load pricing table from JSON file.", e);
        }
    }

    private void loadPricingTable() throws IOException {
        File pricingTableFile = pricingTableResource.getFile();
        JsonNode productsNode = objectMapper.readTree(pricingTableFile);

        for (JsonNode product : productsNode) {
            String barcode = product.get("barcode").asText();
            String name = product.get("name").asText();
            double priceForOne = product.get("priceForOne").asDouble();
            JsonNode offerNode = product.get("offer");

            Offer offer = null;
            if(!offerNode.isNull()){
                Integer numberOfItemsForOffer = offerNode.get("numberOfItemsForOffer").asInt();
                Double sumOfItemsOffer = offerNode.get("sumOfItemsOffer").asDouble();
                offer = Offer.builder().
                        numberOfItemsForOffer(numberOfItemsForOffer).
                        sumOfItemsOffer(sumOfItemsOffer).
                        build();
            }

            ProductPricingData productPricingData = ProductPricingData.builder().
                    name(name).
                    priceForOne(priceForOne)
                    .offer(offer)
                    .build();

            productPriceMap.put(barcode, productPricingData);
        }

        log.info("Product prices loaded successfully: {}", productPriceMap);
    }

//    public Double calculateProductSum(String[] barCodes){
//
//    }
}
