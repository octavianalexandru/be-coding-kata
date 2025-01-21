package com.hailo_demo.be_coding_kata.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

/**
 * DTO for pricing information that currently is persisted in a json file.
 */
@Data
@Builder
public class ProductPricingData {
    @NonNull
    private String name;
    @NonNull
    private Double priceForOne;
    private Offer offer;
}
