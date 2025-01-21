package com.hailo_demo.be_coding_kata.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CheckoutDTO {

    private List<String> barCodes;
}
