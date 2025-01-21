package com.hailo_demo.be_coding_kata.controller;

import com.hailo_demo.be_coding_kata.domain.dto.CheckoutDTO;
import com.hailo_demo.be_coding_kata.domain.dto.ErrorDTO;
import com.hailo_demo.be_coding_kata.exception.BarcodesForCheckoutListEmpty;
import com.hailo_demo.be_coding_kata.exception.ProductNotRegistered;
import com.hailo_demo.be_coding_kata.service.CheckoutService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/checkout")
public class CheckoutController {

    private final CheckoutService checkoutService;

    @PostMapping
    public ResponseEntity<?> checkout(@RequestBody CheckoutDTO checkoutDTO){
        try {
            Double totalPrice = checkoutService.getPrice(checkoutDTO.getBarCodes());
            return ResponseEntity.ok(totalPrice);
        } catch (BarcodesForCheckoutListEmpty e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDTO("No product sent for price calculation", "haiilo.noProduct"));
        }catch (ProductNotRegistered e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDTO("One of the barcodes is missing from our pricing list", "haiilo.productNotRegistered"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDTO("Unkown error", "haiilo.unkownError"));
        }
    }
}
