package com.thuc.rooms.controller;

import com.thuc.rooms.constants.DiscountConstant;
import com.thuc.rooms.dto.DiscountCarsDto;
import com.thuc.rooms.dto.SuccessResponseDto;
import com.thuc.rooms.dto.UserDiscountCarDto;
import com.thuc.rooms.service.IDiscountCarsService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/discount-cars")
@RequiredArgsConstructor
public class DiscountCarsController {
    private final IDiscountCarsService discountCarsService;
    private final Logger logger = LoggerFactory.getLogger(DiscountCarsController.class);
    @GetMapping("")
    public ResponseEntity<?> getAllDiscountCars() {
        logger.debug("Request to get all discount cars");
        SuccessResponseDto response = SuccessResponseDto.builder()
                .code(DiscountConstant.STATUS_200)
                .message(DiscountConstant.MESSAGE_200)
                .data(discountCarsService.getAllDiscountCars())
                .build();
        return ResponseEntity.ok(response);
    }
    @GetMapping("/my-discounts/{email}")
    public ResponseEntity<?> getMyDiscountCars(@PathVariable String email) {
        logger.debug("Request to get my discount cars");
        SuccessResponseDto response = SuccessResponseDto.builder()
                .code(DiscountConstant.STATUS_200)
                .message(DiscountConstant.MESSAGE_200)
                .data(discountCarsService.getAllMyDiscounts(email))
                .build();
        return ResponseEntity.ok(response);
    }
    @PostMapping("/save-discount")
    public ResponseEntity<?> saveDiscountCars(@RequestBody UserDiscountCarDto userDiscountCarDto) {
        logger.debug("Request to save discount cars");
        SuccessResponseDto response = SuccessResponseDto.builder()
                .code(DiscountConstant.STATUS_200)
                .message(DiscountConstant.MESSAGE_200)
                .data(discountCarsService.saveDiscount(userDiscountCarDto))
                .build();
        return ResponseEntity.ok(response);
    }
}
