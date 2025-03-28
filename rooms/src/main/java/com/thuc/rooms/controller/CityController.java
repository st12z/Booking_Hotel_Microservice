package com.thuc.rooms.controller;

import com.thuc.rooms.constants.CityConstant;
import com.thuc.rooms.dto.SuccessResponseDto;
import com.thuc.rooms.service.ICityService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cities")
@RequiredArgsConstructor
public class CityController {
    private final ICityService cityService;
    private final Logger logger = LoggerFactory.getLogger(CityController.class);
    @GetMapping("")
    public ResponseEntity<?> getAllCities() {
        logger.debug("Request to get all cities...");
        SuccessResponseDto response = SuccessResponseDto.builder()
                .code(CityConstant.STATUS_200)
                .message(CityConstant.MESSAGE_200)
                .data(cityService.getAllCities())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
