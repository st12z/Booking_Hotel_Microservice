package com.thuc.rooms.controller;


import com.thuc.rooms.constants.PropertyConstant;
import com.thuc.rooms.dto.SuccessResponseDto;
import com.thuc.rooms.service.IPropertyService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/properties")
@RequiredArgsConstructor
public class PropertiesController {
    private final IPropertyService propertyService;
    private final Logger logger = LoggerFactory.getLogger(PropertiesController.class);
    @GetMapping("")
    public ResponseEntity<?> getAllProperties(@RequestParam String slugCity) {
        logger.debug("Request to get all properties for city {}", slugCity);
        SuccessResponseDto response = SuccessResponseDto.builder()
                .code(PropertyConstant.STATUS_200)
                .message(PropertyConstant.MESSAGE_200)
                .data(propertyService.getAllProperties(slugCity))
                .build();
        return ResponseEntity.ok(response);
    }

}
