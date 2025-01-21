package com.hailo_demo.be_coding_kata.repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hailo_demo.be_coding_kata.domain.Offer;
import com.hailo_demo.be_coding_kata.domain.ProductPricingData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * A repository like component, which deserializes a json file and loads up information about pricing for a list of products at startup(default eager bean init is used).
 * Provides a method to get the deserialized data.
 */
@Slf4j
@Component
public class PricingDataRepository {
    private final ObjectMapper objectMapper;
    private final Resource pricingTableResource;
    private final Map<String, ProductPricingData> productPriceMap = new HashMap<>();


    public PricingDataRepository(ObjectMapper objectMapper, Resource pricingTableResource) {
        this.objectMapper = objectMapper;
        this.pricingTableResource = pricingTableResource;
        loadPricingDataFromJson();
    }

    private void loadPricingDataFromJson() {
        try {
            File file = pricingTableResource.getFile();
            JsonNode productsNode = objectMapper.readTree(file);
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
            log.info("Pricing data loaded: {}", productPriceMap);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load pricing data", e);
        }
    }

    public Map<String, ProductPricingData> getProductPriceMap() {
        return productPriceMap;
    }
}
