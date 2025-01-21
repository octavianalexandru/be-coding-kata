package com.hailo_demo.be_coding_kata.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class ProductPricingData {
    @NonNull
    private String name;
    @NonNull
    private Double priceForOne;
    private Offer offer;
}
