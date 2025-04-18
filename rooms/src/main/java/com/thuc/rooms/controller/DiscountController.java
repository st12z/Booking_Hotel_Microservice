package com.thuc.rooms.constants;

import com.thuc.rooms.dto.SuccessResponseDto;
import com.thuc.rooms.dto.UserDiscountDto;
import com.thuc.rooms.service.IUserDiscountService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/discounts")
@RestController
@RequiredArgsConstructor
public class DiscountController {
    private final Logger logger = LoggerFactory.getLogger(DiscountController.class);
    private final IUserDiscountService userDiscountService;
    @PostMapping("")
    public ResponseEntity<?> saveDiscount(@RequestBody UserDiscountDto userDiscountDto) {
        logger.info("UserDiscountDto: {}", userDiscountDto);
        SuccessResponseDto success = SuccessResponseDto.builder()
                .code(200)
                .message("Save discount success")
                .data(userDiscountService.saveDiscount(userDiscountDto))
                .build();
        return ResponseEntity.ok(success);
    }
}
