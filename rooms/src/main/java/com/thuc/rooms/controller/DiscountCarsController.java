package com.thuc.rooms.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thuc.rooms.constants.DiscountConstant;
import com.thuc.rooms.dto.*;
import com.thuc.rooms.service.IDiscountCarsService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/api/discount-cars")
@RequiredArgsConstructor
public class DiscountCarsController {
    private final IDiscountCarsService discountCarsService;
    private final Logger logger = LoggerFactory.getLogger(DiscountCarsController.class);
    @GetMapping("")
    public ResponseEntity<SuccessResponseDto<List<DiscountCarsDto>>> getAllDiscountCars() {
        logger.debug("Request to get all discount cars");
        SuccessResponseDto<List<DiscountCarsDto>> response = SuccessResponseDto.<List<DiscountCarsDto>>builder()
                .code(DiscountConstant.STATUS_200)
                .message(DiscountConstant.MESSAGE_200)
                .data(discountCarsService.getAllDiscountCars())
                .build();
        return ResponseEntity.ok(response);
    }
    @GetMapping("my-discounts")
    public ResponseEntity<SuccessResponseDto<List<DiscountCarsDto>>> getMyDiscountCars(@RequestHeader("X-User-Email") String email) {
        logger.debug("Request to get my discount cars");
        SuccessResponseDto<List<DiscountCarsDto>> response = SuccessResponseDto.<List<DiscountCarsDto>>builder()
                .code(DiscountConstant.STATUS_200)
                .message(DiscountConstant.MESSAGE_200)
                .data(discountCarsService.getAllMyDiscounts(email))
                .build();
        return ResponseEntity.ok(response);
    }
    @GetMapping("my-discounts-page")
    public ResponseEntity<SuccessResponseDto<PageResponseDto<List<DiscountCarsDto>>>> getMyDiscountsByPage(
            @RequestHeader("X-User-Email") String email,
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize
    ){
        logger.debug("Request to get my discounts page with email {}, pageNo {}, pageSize {}", email, pageNo, pageSize);
        SuccessResponseDto<PageResponseDto<List<DiscountCarsDto>>> response = SuccessResponseDto.<PageResponseDto<List<DiscountCarsDto>>>builder()
                .code(DiscountConstant.STATUS_200)
                .message(DiscountConstant.MESSAGE_200)
                .data(discountCarsService.getMyDiscountsByEmailPage(email,pageNo,pageSize))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }
    @PostMapping("save-discount")
    public ResponseEntity<SuccessResponseDto<UserDiscountCarDto>> saveDiscountCars(@RequestBody UserDiscountCarDto userDiscountCarDto) {
        logger.debug("Request to save discount cars");
        SuccessResponseDto<UserDiscountCarDto> response = SuccessResponseDto.<UserDiscountCarDto>builder()
                .code(DiscountConstant.STATUS_200)
                .message(DiscountConstant.MESSAGE_200)
                .data(discountCarsService.saveDiscount(userDiscountCarDto))
                .build();
        return ResponseEntity.ok(response);
    }
    @PostMapping("filter")
    public ResponseEntity<SuccessResponseDto<PageResponseDto<List<DiscountCarsDto>>>> getAllDiscountCarsByPage(
            @RequestBody FilterDiscountCarDto filterDto
    ) throws ParseException {
        logger.debug("Request to save discount cars");
        SuccessResponseDto<PageResponseDto<List<DiscountCarsDto>>> response = SuccessResponseDto.<PageResponseDto<List<DiscountCarsDto>>>builder()
                .code(DiscountConstant.STATUS_200)
                .message(DiscountConstant.MESSAGE_200)
                .data(discountCarsService.getAllDiscountsByPage(filterDto))
                .build();
        return ResponseEntity.ok(response);
    }
    @GetMapping("search")
    public ResponseEntity<SuccessResponseDto<PageResponseDto<List<DiscountCarsDto>>>> getSearchDiscountCars(
            @RequestParam(defaultValue = "",required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize
    ){
        logger.debug("Request to search discount cars");
        SuccessResponseDto<PageResponseDto<List<DiscountCarsDto>>> response = SuccessResponseDto.<PageResponseDto<List<DiscountCarsDto>>>builder()
                .code(DiscountConstant.STATUS_200)
                .message(DiscountConstant.MESSAGE_200)
                .data(discountCarsService.getSearchDiscountsByPage(keyword,pageNo,pageSize))
                .build();
        return ResponseEntity.ok(response);
    }
    @GetMapping("{id}")
    public ResponseEntity<SuccessResponseDto<DiscountCarsDto>> getDiscountCarById(@PathVariable("id") Integer id) {
        logger.debug("Request get discount car by id {}", id);
        SuccessResponseDto<DiscountCarsDto> response = SuccessResponseDto.<DiscountCarsDto>builder()
                .code(DiscountConstant.STATUS_200)
                .message(DiscountConstant.MESSAGE_200)
                .data(discountCarsService.getDiscountCarId(id))
                .build();
        return ResponseEntity.ok(response);
    }
    @PostMapping("update/{id}")
    public ResponseEntity<SuccessResponseDto<DiscountCarsDto>> updateDiscountCarById(
            @PathVariable("id") Integer id,
            @RequestPart String discountCar,
            @RequestPart MultipartFile file

    ) throws JsonProcessingException {
        logger.debug("Request to update discount car by id {}", id);
        ObjectMapper objectMapper = new ObjectMapper();
        DiscountCarRequestDto discountCarRequestDto = objectMapper.readValue(discountCar, DiscountCarRequestDto.class);
        SuccessResponseDto<DiscountCarsDto> response = SuccessResponseDto.<DiscountCarsDto>builder()
                .code(DiscountConstant.STATUS_200)
                .message(DiscountConstant.MESSAGE_200)
                .data(discountCarsService.updateDiscountCar(id,discountCarRequestDto,file))
                .build();
        return ResponseEntity.ok(response);
    }
    @PostMapping("create")
    public ResponseEntity<SuccessResponseDto<DiscountCarsDto>> createDiscountCar(@RequestPart String discountCar,
                                                                                 @RequestPart MultipartFile file
    ) throws JsonProcessingException {
        logger.debug("Request to create discount car");
        ObjectMapper objectMapper = new ObjectMapper();
        DiscountCarRequestDto discountCarRequestDto = objectMapper.readValue(discountCar, DiscountCarRequestDto.class);
        SuccessResponseDto<DiscountCarsDto> response = SuccessResponseDto.<DiscountCarsDto>builder()
                .code(DiscountConstant.STATUS_200)
                .message(DiscountConstant.MESSAGE_200)
                .data(discountCarsService.createDiscountCar(discountCarRequestDto,file))
                .build();
        return ResponseEntity.ok(response);
    }
}
