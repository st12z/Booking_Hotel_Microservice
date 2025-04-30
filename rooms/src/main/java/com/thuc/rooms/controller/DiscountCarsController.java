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

import java.util.List;

@RestController
@RequestMapping("/api/discount-cars")
@RequiredArgsConstructor
public class DiscountCarsController {
    private final IDiscountCarsService discountCarsService;
    private final Logger logger = LoggerFactory.getLogger(DiscountCarsController.class);
    @GetMapping("")
    public ResponseEntity<SuccessResponseDto<List<DiscountCarsDto>>> getAllDiscountCars() {
        logger.debug("Request to get all discount cars");
        SuccessResponseDto<List<DiscountCarsDto>> response = SuccessResponseDto.<List<DiscountCarsDto>>builder()
                .code(DiscountConstant.STATUS_200)
                .message(DiscountConstant.MESSAGE_200)
                .data(discountCarsService.getAllDiscountCars())
                .build();
        return ResponseEntity.ok(response);
    }
    @GetMapping("/my-discounts/{email}")
    public ResponseEntity<SuccessResponseDto<List<DiscountCarsDto>>> getMyDiscountCars(@PathVariable String email) {
        logger.debug("Request to get my discount cars");
        SuccessResponseDto<List<DiscountCarsDto>> response = SuccessResponseDto.<List<DiscountCarsDto>>builder()
                .code(DiscountConstant.STATUS_200)
                .message(DiscountConstant.MESSAGE_200)
                .data(discountCarsService.getAllMyDiscounts(email))
                .build();
        return ResponseEntity.ok(response);
    }
    @PostMapping("/save-discount")
    public ResponseEntity<SuccessResponseDto<UserDiscountCarDto>> saveDiscountCars(@RequestBody UserDiscountCarDto userDiscountCarDto) {
        logger.debug("Request to save discount cars");
        SuccessResponseDto<UserDiscountCarDto> response = SuccessResponseDto.<UserDiscountCarDto>builder()
                .code(DiscountConstant.STATUS_200)
                .message(DiscountConstant.MESSAGE_200)
                .data(discountCarsService.saveDiscount(userDiscountCarDto))
                .build();
        return ResponseEntity.ok(response);
    }
}
