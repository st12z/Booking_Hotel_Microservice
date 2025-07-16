package com.thuc.rooms.controller;

import com.thuc.rooms.constants.PropertyConstant;
import com.thuc.rooms.dto.*;
import com.thuc.rooms.service.IPropertyTypeService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/property-types")
@RequiredArgsConstructor
public class PropertyTypeController {
    private final Logger logger = LoggerFactory.getLogger(PropertyTypeController.class);
    private final IPropertyTypeService propertyTypeService;
    @GetMapping("")
    public ResponseEntity<SuccessResponseDto<List<String>>> getPropertyTypes() {
        logger.debug("getPropertyTypes");
        SuccessResponseDto<List<String>> response = SuccessResponseDto.<List<String>>builder()
                .code(PropertyConstant.STATUS_200)
                .message(PropertyConstant.MESSAGE_200)
                .data(propertyTypeService.getAllPropertyTypes())
                .build();
        return ResponseEntity.ok(response);
    }
    @GetMapping("search")
    public ResponseEntity<SuccessResponseDto<PageResponseDto<List<PropertyTypeDto>>>> getPropertyTypesByPage(
            @RequestParam(defaultValue = "",required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        logger.debug("getPropertyTypes pageNo:{},pageSize:{}", pageNo, pageSize);
        SuccessResponseDto<PageResponseDto<List<PropertyTypeDto>>> response = SuccessResponseDto.<PageResponseDto<List<PropertyTypeDto>>>builder()
                .code(PropertyConstant.STATUS_200)
                .message(PropertyConstant.MESSAGE_200)
                .data(propertyTypeService.getAllPropertyTypesByPage(keyword,pageNo,pageSize))
                .build();
        return ResponseEntity.ok(response);
    }
    @GetMapping("{id}")
    public ResponseEntity<SuccessResponseDto<PropertyTypeDto>> getPropertyTypeById(@PathVariable Integer id){
        logger.debug("getPropertyType id:{}", id);
        SuccessResponseDto<PropertyTypeDto> response = SuccessResponseDto.<PropertyTypeDto>builder()
                .code(PropertyConstant.STATUS_200)
                .message(PropertyConstant.MESSAGE_200)
                .data(propertyTypeService.getPropertyTypeById(id))
                .build();
        return ResponseEntity.ok(response);
    }
    @PostMapping("/update/{id}")
    public ResponseEntity<SuccessResponseDto<PropertyTypeDto>> updatePropertyType(@PathVariable Integer id,
                                                                                  @RequestBody PropertyTypeRequestDto propertyTypeDto
    ){
        logger.debug("updatePropertyType id:{}", id);
        SuccessResponseDto<PropertyTypeDto> response = SuccessResponseDto.<PropertyTypeDto>builder()
                .code(PropertyConstant.STATUS_200)
                .message(PropertyConstant.MESSAGE_200)
                .data(propertyTypeService.updatePropertyType(id,propertyTypeDto))
                .build();
        return ResponseEntity.ok(response);
    }
    @PostMapping("/create")
    public ResponseEntity<SuccessResponseDto<PropertyTypeDto>> createPropertyTyp(@RequestBody PropertyTypeRequestDto propertyTypeDto
    ){

        SuccessResponseDto<PropertyTypeDto> response = SuccessResponseDto.<PropertyTypeDto>builder()
                .code(PropertyConstant.STATUS_200)
                .message(PropertyConstant.MESSAGE_200)
                .data(propertyTypeService.createPropertyType(propertyTypeDto))
                .build();
        return ResponseEntity.ok(response);
    }
}
