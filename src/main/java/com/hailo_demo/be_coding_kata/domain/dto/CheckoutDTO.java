package com.hailo_demo.be_coding_kata.domain.dto;

import lombok.Data;

import java.util.ArrayList;

@Data
public class CheckoutDTO {

    private ArrayList<String> barCodes;
}
