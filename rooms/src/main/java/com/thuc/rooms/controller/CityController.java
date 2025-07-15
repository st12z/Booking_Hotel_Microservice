package com.thuc.rooms.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thuc.rooms.constants.CityConstant;
import com.thuc.rooms.dto.CityDto;
import com.thuc.rooms.dto.CityRequestDto;
import com.thuc.rooms.dto.PageResponseDto;
import com.thuc.rooms.dto.SuccessResponseDto;
import com.thuc.rooms.service.ICityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/cities")
@RequiredArgsConstructor
public class CityController {
    private final ICityService cityService;
    private final Logger logger = LoggerFactory.getLogger(CityController.class);
    @GetMapping("")
    public ResponseEntity<SuccessResponseDto<List<CityDto>>> getAllCities() {
        logger.debug("Request to get all cities...");
        SuccessResponseDto<List<CityDto>> response = SuccessResponseDto
                .<List<CityDto>>builder()
                .code(CityConstant.STATUS_200)
                .message(CityConstant.MESSAGE_200)
                .data(cityService.getAllCities())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @GetMapping("/city-page")
    public ResponseEntity<SuccessResponseDto<PageResponseDto<List<CityDto>>>> getAllCitiesPage(
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize
            ) {
        logger.debug("Request to get all cities...");
        SuccessResponseDto<PageResponseDto<List<CityDto>>> response = SuccessResponseDto.<PageResponseDto<List<CityDto>>>builder()
                .code(CityConstant.STATUS_200)
                .message(CityConstant.MESSAGE_200)
                .data(cityService.getAllCitiesPageResponse(pageNo,pageSize))
                .build();
        return ResponseEntity.ok(response);
    }
    @GetMapping("/search")
    public ResponseEntity<SuccessResponseDto<PageResponseDto<List<CityDto>>>> getAllCitiesSearch(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize
            ){
        logger.debug("Request to get all cities with keyword {}, pageNo {}, pageSize {}",keyword,pageNo,pageSize);
        SuccessResponseDto<PageResponseDto<List<CityDto>>> response = SuccessResponseDto.<PageResponseDto<List<CityDto>>>builder()
                .code(CityConstant.STATUS_200)
                .message(CityConstant.MESSAGE_200)
                .data(cityService.getAllCitiesByKeyword(keyword,pageNo,pageSize))
                .build();
        return ResponseEntity.ok(response);
    }
    @GetMapping("{id}")
    public ResponseEntity<SuccessResponseDto<CityDto>> getCityById(@PathVariable Integer id) {
        logger.debug("Request to get city by id {}", id);
        SuccessResponseDto<CityDto> response = SuccessResponseDto.<CityDto>builder()
                .code(CityConstant.STATUS_200)
                .message(CityConstant.MESSAGE_200)
                .data(cityService.getCityById(id))
                .build();
        return ResponseEntity.ok(response);
    }
    @PostMapping("/update/{id}")
    public ResponseEntity<SuccessResponseDto<CityDto>> updateCity(@PathVariable Integer id,
                                                                  @RequestPart  String name,
                                                                  @RequestPart MultipartFile image) {
        logger.debug("Request to update city by id {}", id);
        SuccessResponseDto<CityDto> response = SuccessResponseDto.<CityDto>builder()
                .code(CityConstant.STATUS_200)
                .message(CityConstant.MESSAGE_200)
                .data(cityService.updateCity(id,name,image))
                .build();
        return ResponseEntity.ok(response);

    }
    @PostMapping("/create")
    public ResponseEntity<SuccessResponseDto<CityDto>> createCity(@RequestPart String city,@RequestPart MultipartFile image) throws JsonProcessingException {
        logger.debug("Request to create city {}", city);
        ObjectMapper objectMapper = new ObjectMapper();
        CityRequestDto cityDto = objectMapper.readValue(city, CityRequestDto.class);
        SuccessResponseDto<CityDto> response = SuccessResponseDto.<CityDto>builder()
                .code(CityConstant.STATUS_200)
                .message(CityConstant.MESSAGE_200)
                .data(cityService.createCity(cityDto,image))
                .build();
        return ResponseEntity.ok(response);

    }
}
