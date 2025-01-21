package com.hailo_demo.be_coding_kata.config;

import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;



/**
 * Configuration class which currently instantiates one resource that loads up the pricing-table.json
 */
@org.springframework.context.annotation.Configuration
public class Configuration {
    @Bean
    public Resource pricingTableResource() {
        return new ClassPathResource("pricing-table.json");
    }
}
