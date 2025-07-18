package com.thuc.rooms.controller;

import com.thuc.rooms.constants.FacilityConstant;
import com.thuc.rooms.dto.FacilitiesDto;
import com.thuc.rooms.dto.FacilityDto;
import com.thuc.rooms.dto.PageResponseDto;
import com.thuc.rooms.dto.SuccessResponseDto;
import com.thuc.rooms.service.IFacilitiesService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("search")
    public ResponseEntity<SuccessResponseDto<PageResponseDto<List<FacilitiesDto>>>> searchFacilities(
            @RequestParam(defaultValue = "",required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        logger.debug(" search facilities keyword {}, pageNo {}, pageSize {}", keyword, pageNo, pageSize);
        SuccessResponseDto<PageResponseDto<List<FacilitiesDto>>> response = SuccessResponseDto.<PageResponseDto<List<FacilitiesDto>>>builder()
                .code(FacilityConstant.STATUS_200)
                .message(FacilityConstant.MESSAGE_200)
                .data(facilitiesService.getAllFacilitiesPage(keyword,pageNo,pageSize))
                .build();
        return ResponseEntity.ok().body(response);
    }
    @GetMapping("{id}")
    public ResponseEntity<SuccessResponseDto<FacilitiesDto>> getFacilityById(@PathVariable Integer id) {
        logger.debug(" get facilities by id {}", id);
        SuccessResponseDto<FacilitiesDto> response = SuccessResponseDto.<FacilitiesDto>builder()
                .code(FacilityConstant.STATUS_200)
                .message(FacilityConstant.MESSAGE_200)
                .data(facilitiesService.getFacilityById(id))
                .build();
        return ResponseEntity.ok().body(response);
    }
    @PostMapping("update/{id}")
    public ResponseEntity<SuccessResponseDto<FacilitiesDto>> updateFacilityById(@PathVariable Integer id, @RequestBody FacilityDto facilityDto){
        logger.debug(" update facilities by id {}", id);
        SuccessResponseDto<FacilitiesDto> response = SuccessResponseDto.<FacilitiesDto>builder()
                .code(FacilityConstant.STATUS_200)
                .message(FacilityConstant.MESSAGE_200)
                .data(facilitiesService.update(id,facilityDto))
                .build();
        return ResponseEntity.ok().body(response);
    }
    @PostMapping("create")
    public ResponseEntity<SuccessResponseDto<FacilitiesDto>> createFacility(@RequestBody FacilityDto facilityDto){
        logger.debug(" create facilities by {}", facilityDto);
        SuccessResponseDto<FacilitiesDto> response = SuccessResponseDto.<FacilitiesDto>builder()
                .code(FacilityConstant.STATUS_200)
                .message(FacilityConstant.MESSAGE_200)
                .data(facilitiesService.create(facilityDto))
                .build();
        return ResponseEntity.ok().body(response);
    }
}
