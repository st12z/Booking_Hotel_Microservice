package com.thuc.rooms.controller;

import com.thuc.rooms.constants.PropertyConstant;
import com.thuc.rooms.dto.SuccessResponseDto;
import com.thuc.rooms.utils.PropertyEnum;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/property-types")
@RequiredArgsConstructor
public class PropertyTypeController {
    private final Logger logger = LoggerFactory.getLogger(PropertyTypeController.class);
    @GetMapping("")
    public ResponseEntity<SuccessResponseDto<List<String>>> getPropertyTypes() {
        logger.debug("getPropertyTypes");
        SuccessResponseDto<List<String>> response = SuccessResponseDto.<List<String>>builder()
                .code(PropertyConstant.STATUS_200)
                .message(PropertyConstant.MESSAGE_200)
                .data(PropertyEnum.getProperties())
                .build();
        return ResponseEntity.ok(response);
    }
}
