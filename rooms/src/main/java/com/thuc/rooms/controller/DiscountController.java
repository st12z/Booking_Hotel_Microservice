package com.thuc.rooms.controller;

import com.thuc.rooms.constants.DiscountConstant;
import com.thuc.rooms.dto.SuccessResponseDto;
import com.thuc.rooms.service.IDiscountService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/discounts")
public class DiscountController {
    private final IDiscountService discountService;
    private final Logger log = LoggerFactory.getLogger(DiscountController.class);
    @GetMapping("")
    public ResponseEntity<?> getAllDiscounts() {
        log.debug("Request to get all discounts");
        SuccessResponseDto response = SuccessResponseDto.builder()
                .code(DiscountConstant.STATUS_200)
                .message(DiscountConstant.MESSAGE_200)
                .data(discountService.getAllDiscounts())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
