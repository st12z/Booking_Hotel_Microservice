package com.thuc.rooms.controller;


import com.thuc.rooms.constants.PropertyConstant;
import com.thuc.rooms.dto.PageResponseDto;
import com.thuc.rooms.dto.PropertyDto;
import com.thuc.rooms.dto.SuccessResponseDto;
import com.thuc.rooms.service.IPropertyService;
import jakarta.ws.rs.POST;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/properties")
@RequiredArgsConstructor
public class PropertiesController {
    private final IPropertyService propertyService;
    private final Logger logger = LoggerFactory.getLogger(PropertiesController.class);
    @GetMapping("/all")
    public ResponseEntity<
            SuccessResponseDto<PageResponseDto<List<PropertyDto>>>
            > getAllProperties(@RequestParam(defaultValue = "1") Integer pageNo,
                               @RequestParam(defaultValue = "10") Integer pageSize
                               ) {
        logger.debug("Request to get all properties ");
        SuccessResponseDto<PageResponseDto<List<PropertyDto>>> response = SuccessResponseDto.<PageResponseDto<List<PropertyDto>>>builder()
                .message(PropertyConstant.MESSAGE_200)
                .code(PropertyConstant.STATUS_200)
                .data(propertyService.getAllProperties(pageNo,pageSize))
                .build();
        return ResponseEntity.ok(response);
    }
    @GetMapping("")
    public ResponseEntity<SuccessResponseDto<List<PropertyDto>>> getAllPropertiesBySlugCity(@RequestParam String slugCity) {
        logger.debug("Request to get all properties for city {}", slugCity);
        SuccessResponseDto<List<PropertyDto>> response = SuccessResponseDto.<List<PropertyDto>>builder()
                .code(PropertyConstant.STATUS_200)
                .message(PropertyConstant.MESSAGE_200)
                .data(propertyService.getAllPropertiesBySlugCity(slugCity))
                .build();
        return ResponseEntity.ok(response);
    }
    @GetMapping("/slug/{slug}")
    public ResponseEntity<SuccessResponseDto<PropertyDto>> getProperties(@PathVariable String slug) {
        logger.debug("Request to get properties for city {}", slug);
        SuccessResponseDto<PropertyDto> response =SuccessResponseDto.<PropertyDto>builder()
                .message(PropertyConstant.MESSAGE_200)
                .code(PropertyConstant.STATUS_200)
                .data(propertyService.getPropertyBySlug(slug))
                .build();
        return ResponseEntity.ok(response);
    }
    @PostMapping("")
    public ResponseEntity<SuccessResponseDto<List<PropertyDto>>> addProperty(@RequestBody List<String> slugs) {
        logger.debug("Request to get properties with list slugs {}", slugs);
        SuccessResponseDto<List<PropertyDto>> response =SuccessResponseDto
                .<List<PropertyDto>>builder()
                .message(PropertyConstant.MESSAGE_200)
                .code(PropertyConstant.STATUS_200)
                .data(propertyService.getPropertiesBySlugs(slugs))
                .build();
        return ResponseEntity.ok(response);
    }
    @GetMapping("/id/{id}")
    public ResponseEntity<?> getPropertyId(@PathVariable Integer id) {
        logger.debug("Request to get properties by  id {}", id);
        SuccessResponseDto<PropertyDto> response = SuccessResponseDto.<PropertyDto>builder()
                .message(PropertyConstant.MESSAGE_200)
                .code(PropertyConstant.STATUS_200)
                .data(propertyService.getPropertyById(id))
                .build();
        return ResponseEntity.ok(response);
    }
    @GetMapping("/amount-properties")
    public ResponseEntity<SuccessResponseDto<Integer>> amountProperties() {
        logger.debug("Request to get properties by  amount properties");
        SuccessResponseDto<Integer> response = SuccessResponseDto.<Integer>builder()
                .code(PropertyConstant.STATUS_200)
                .message(PropertyConstant.MESSAGE_200)
                .data(propertyService.getAmountProperties())
                .build();
        return ResponseEntity.ok(response);
    }
    @GetMapping("/search")
    public ResponseEntity<
            SuccessResponseDto<PageResponseDto<List<PropertyDto>>>
            > getPropertiesByKeyword(@RequestParam(defaultValue = "") String keyword,
                                                                                        @RequestParam(defaultValue = "1") Integer pageNo,
                                                                                        @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        logger.debug("Request to get properties by  keyword {}", keyword);
        SuccessResponseDto<PageResponseDto<List<PropertyDto>>> response = SuccessResponseDto.<PageResponseDto<List<PropertyDto>>>builder()
                .message(PropertyConstant.MESSAGE_200)
                .code(PropertyConstant.STATUS_200)
                .data(propertyService.getPropertiesByKeyword(keyword,pageNo,pageSize))
                .build();
        return ResponseEntity.ok(response);
    }
}
