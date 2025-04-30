package com.thuc.rooms.controller;

import com.thuc.rooms.constants.DiscountConstant;
import com.thuc.rooms.dto.DiscountDto;
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

import java.util.List;

@RequestMapping("/api/discounts")
@RestController
@RequiredArgsConstructor
public class DiscountController {
    private final Logger logger = LoggerFactory.getLogger(DiscountController.class);
    private final IDiscountService discountService;
    @GetMapping("")
    public ResponseEntity<SuccessResponseDto<List<DiscountDto>>> getAllDiscounts() {
        logger.debug("Request to get all discounts");
        SuccessResponseDto<List<DiscountDto>> response = SuccessResponseDto.<List<DiscountDto>>builder()
                .code(DiscountConstant.STATUS_200)
                .message(DiscountConstant.MESSAGE_200)
                .data(discountService.getAllDiscounts())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @PostMapping("/save")
    public ResponseEntity<SuccessResponseDto<UserDiscountDto> > saveDiscount(@RequestBody UserDiscountDto userDiscountDto) {
        logger.info("UserDiscountDto: {}", userDiscountDto);
        SuccessResponseDto<UserDiscountDto> success = SuccessResponseDto.<UserDiscountDto>builder()
                .code(200)
                .message("Save discount success")
                .data(discountService.saveDiscount(userDiscountDto))
                .build();
        return ResponseEntity.ok(success);
    }
    @GetMapping("/my-discounts/{email}")
    public ResponseEntity<SuccessResponseDto<List<DiscountDto>>> getMyDiscounts(@PathVariable @Email String email){
        logger.debug("Request to get my discounts with email {}", email);
        SuccessResponseDto<List<DiscountDto>> response = SuccessResponseDto.<List<DiscountDto>>builder()
                .code(DiscountConstant.STATUS_200)
                .message(DiscountConstant.MESSAGE_200)
                .data(discountService.getMyDiscountsByEmail(email))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
