package com.hailo_demo.be_coding_kata.service;

import com.hailo_demo.be_coding_kata.domain.ProductPricingData;
import com.hailo_demo.be_coding_kata.exception.BarcodesForCheckoutListEmpty;
import com.hailo_demo.be_coding_kata.exception.ProductNotRegistered;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
public class CheckoutService {

    private final PricingDataService pricingDataService;

    public Double getPrice(List<String> barCodes) {
        if(barCodes.isEmpty())
            throw new BarcodesForCheckoutListEmpty();
        Double sum = 0D;
        Map<String, Integer> productNumberMap = new HashMap<>();

        barCodes.forEach(barCode -> {
            productNumberMap.put(barCode, productNumberMap.getOrDefault(barCode, 0) + 1);
        });

        for (Map.Entry<String, Integer> entry : productNumberMap.entrySet()) {
            String barcode = entry.getKey();
            int numberOfItems = entry.getValue();
            ProductPricingData productPricingData = pricingDataService.getProductPricing(barcode);

            if (productPricingData != null) {
                if (productPricingData.getOffer() == null) {
                    sum += productPricingData.getPriceForOne() * numberOfItems;
                } else {
                    if (productPricingData.getOffer().getNumberOfItemsForOffer() <= numberOfItems) {
                        int offerCount = numberOfItems / productPricingData.getOffer().getNumberOfItemsForOffer();
                        int remainingItems = numberOfItems % productPricingData.getOffer().getNumberOfItemsForOffer();

                        sum += offerCount * productPricingData.getOffer().getSumOfItemsOffer();
                        sum += remainingItems * productPricingData.getPriceForOne();
                    } else {
                        sum += productPricingData.getPriceForOne() * numberOfItems;
                    }
                }
            } else {
                throw new ProductNotRegistered();
            }
        }

        return sum;
    }
}
