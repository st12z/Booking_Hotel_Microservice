package com.thuc.rooms.controller;


import com.thuc.rooms.constants.PropertyConstant;
import com.thuc.rooms.dto.SuccessResponseDto;
import com.thuc.rooms.service.IPropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/properties")
@RequiredArgsConstructor
public class PropertiesController {
    private final IPropertyService propertyService;
    @GetMapping("")
    public ResponseEntity<?> getAllProperties() {
        SuccessResponseDto response = SuccessResponseDto.builder()
                .code(PropertyConstant.STATUS_200)
                .message(PropertyConstant.MESSAGE_200)
                .data(propertyService.getAllProperties())
                .build();
        return ResponseEntity.ok(response);
    }
}
