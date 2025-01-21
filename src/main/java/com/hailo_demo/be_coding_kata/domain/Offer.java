package com.hailo_demo.be_coding_kata.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

/**
 * Domain class for offer pricing details. Part of ProductPricingData.
 */
@Data
@Builder
public class Offer {
    @NonNull
    private Integer numberOfItemsForOffer;
    @NonNull
    private Double sumOfItemsOffer;
}
