package com.thuc.rooms.controller;

import com.thuc.rooms.dto.FacilitiesDto;
import com.thuc.rooms.dto.SuccessResponseDto;
import com.thuc.rooms.service.IFacilitiesService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/facilities")
@RequiredArgsConstructor
public class FacilitiesController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final IFacilitiesService facilitiesService;
    @GetMapping
    public ResponseEntity<SuccessResponseDto<List<FacilitiesDto>>> getFacilities() {
        logger.debug("getFacilities...");
        SuccessResponseDto<List<FacilitiesDto>> successResponseDto =SuccessResponseDto.<List<FacilitiesDto>>builder()
                .message("success")
                .code(200)
                .data(facilitiesService.geAllFacilities())
                .build();
        return ResponseEntity.ok().body(successResponseDto);
    }
}
