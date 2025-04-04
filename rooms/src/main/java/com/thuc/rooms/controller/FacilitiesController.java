package com.thuc.rooms.controller;

import com.thuc.rooms.dto.SuccessResponseDto;
import com.thuc.rooms.utils.HotelFacility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/facilities")
public class FacilitiesController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @GetMapping
    public ResponseEntity<?> getFacilities() {
        logger.debug("getFacilities...");
        SuccessResponseDto successResponseDto =SuccessResponseDto.builder()
                .message("success")
                .code(200)
                .data(HotelFacility.getFacilities())
                .build();
        return ResponseEntity.ok().body(successResponseDto);
    }
}
