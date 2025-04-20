package com.thuc.rooms.controller;

import com.thuc.rooms.constants.DiscountConstant;
import com.thuc.rooms.dto.SuccessResponseDto;
import com.thuc.rooms.dto.UserDiscountDto;
import com.thuc.rooms.service.IDiscountService;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/discounts")
@RestController
@RequiredArgsConstructor
public class DiscountController {
    private final Logger logger = LoggerFactory.getLogger(DiscountController.class);
    private final IDiscountService discountService;
    @GetMapping("")
    public ResponseEntity<?> getAllDiscounts() {
        logger.debug("Request to get all discounts");
        SuccessResponseDto response = SuccessResponseDto.builder()
                .code(DiscountConstant.STATUS_200)
                .message(DiscountConstant.MESSAGE_200)
                .data(discountService.getAllDiscounts())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @PostMapping("/save")
    public ResponseEntity<?> saveDiscount(@RequestBody UserDiscountDto userDiscountDto) {
        logger.info("UserDiscountDto: {}", userDiscountDto);
        SuccessResponseDto success = SuccessResponseDto.builder()
                .code(200)
                .message("Save discount success")
                .data(discountService.saveDiscount(userDiscountDto))
                .build();
        return ResponseEntity.ok(success);
    }
    @GetMapping("/my-discounts/{email}")
    public ResponseEntity<?> getMyDiscounts(@PathVariable @Email String email){
        logger.debug("Request to get my discounts with email {}", email);
        SuccessResponseDto response = SuccessResponseDto.builder()
                .code(DiscountConstant.STATUS_200)
                .message(DiscountConstant.MESSAGE_200)
                .data(discountService.getMyDiscountsByEmail(email))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
