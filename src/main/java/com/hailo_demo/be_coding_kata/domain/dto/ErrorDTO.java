package com.hailo_demo.be_coding_kata.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorDTO {
    String errorMessage;
    String errorKey;
}
