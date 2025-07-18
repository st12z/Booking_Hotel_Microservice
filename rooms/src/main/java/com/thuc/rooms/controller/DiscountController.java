package com.thuc.rooms.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thuc.rooms.constants.DiscountConstant;
import com.thuc.rooms.dto.*;
import com.thuc.rooms.service.IDiscountService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.List;

@RequestMapping("/api/discounts")
@RestController
@RequiredArgsConstructor
public class DiscountController {
    private final Logger logger = LoggerFactory.getLogger(DiscountController.class);
    private final IDiscountService discountService;
    @GetMapping("")
    public ResponseEntity<SuccessResponseDto<List<DiscountDto>>> getAllDiscounts() {
        logger.debug("Request to get all discounts");
        SuccessResponseDto<List<DiscountDto>> response = SuccessResponseDto.<List<DiscountDto>>builder()
                .code(DiscountConstant.STATUS_200)
                .message(DiscountConstant.MESSAGE_200)
                .data(discountService.getAllDiscounts())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @PostMapping("save")
    public ResponseEntity<SuccessResponseDto<UserDiscountDto> > saveDiscount(@RequestBody UserDiscountDto userDiscountDto) {
        logger.info("UserDiscountDto: {}", userDiscountDto);
        SuccessResponseDto<UserDiscountDto> success = SuccessResponseDto.<UserDiscountDto>builder()
                .code(200)
                .message("Save discount success")
                .data(discountService.saveDiscount(userDiscountDto))
                .build();
        return ResponseEntity.ok(success);
    }
    @GetMapping("my-discounts")
    public ResponseEntity<SuccessResponseDto<List<DiscountDto>>> getMyDiscounts(@RequestHeader("X-User-Email") String email){
        logger.debug("Request to get my discounts with email {}", email);
        SuccessResponseDto<List<DiscountDto>> response = SuccessResponseDto.<List<DiscountDto>>builder()
                .code(DiscountConstant.STATUS_200)
                .message(DiscountConstant.MESSAGE_200)
                .data(discountService.getMyDiscountsByEmail(email))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("my-discounts-page")
    public ResponseEntity<SuccessResponseDto<PageResponseDto<List<DiscountDto>>>> getMyDiscountsByPage(
            @RequestHeader("X-User-Email") String email,
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize
    ){
        logger.debug("Request to get my discounts page with email {}, pageNo {}, pageSize {}", email, pageNo, pageSize);
        SuccessResponseDto<PageResponseDto<List<DiscountDto>>> response = SuccessResponseDto.<PageResponseDto<List<DiscountDto>>>builder()
                .code(DiscountConstant.STATUS_200)
                .message(DiscountConstant.MESSAGE_200)
                .data(discountService.getMyDiscountsByEmailPage(email,pageNo,pageSize))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }
    @GetMapping("discount-types")
    public ResponseEntity<SuccessResponseDto<List<String>>> getAllDiscountTypes(){
        logger.debug("Request to get all discount types");
        SuccessResponseDto<List<String>> response = SuccessResponseDto.<List<String>>builder()
                .code(DiscountConstant.STATUS_200)
                .message(DiscountConstant.MESSAGE_200)
                .data(discountService.getAllDiscountTypes())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @PostMapping("filter")
    public ResponseEntity<SuccessResponseDto<PageResponseDto<List<DiscountDto>>>> getAllDiscounByFilter(@RequestBody FilterDiscountDto filterDto) throws ParseException {
        logger.debug("Request to get all discount by filter {}", filterDto);
        SuccessResponseDto<PageResponseDto<List<DiscountDto>>> response = SuccessResponseDto.<PageResponseDto<List<DiscountDto>>>builder()
                .code(DiscountConstant.STATUS_200)
                .message(DiscountConstant.MESSAGE_200)
                .data(discountService.getAllDiscountsByFilter(filterDto))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @GetMapping("search")
    public ResponseEntity<SuccessResponseDto<PageResponseDto<List<DiscountDto>>>> getSearchDiscounts(
            @RequestParam(defaultValue = "",required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize
    ){
        logger.debug("Request to search discount by keyword {}, pageNo {}, pageSize {}", keyword,pageNo, pageSize);
        SuccessResponseDto<PageResponseDto<List<DiscountDto>>> response = SuccessResponseDto.<PageResponseDto<List<DiscountDto>>>builder()
                .code(DiscountConstant.STATUS_200)
                .message(DiscountConstant.MESSAGE_200)
                .data(discountService.getSearchDiscounts(keyword,pageNo,pageSize))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @GetMapping("{id}")
    public ResponseEntity<SuccessResponseDto<DiscountDto>> getDiscountById(@PathVariable("id") Integer id){
        logger.debug("Request get discount by id {}", id);
        SuccessResponseDto<DiscountDto> response = SuccessResponseDto.<DiscountDto>builder()
                .code(DiscountConstant.STATUS_200)
                .message(DiscountConstant.MESSAGE_200)
                .data(discountService.getDiscountDtoById(id))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @PostMapping("update/{id}")
    public ResponseEntity<SuccessResponseDto<DiscountDto>> updateDiscount(@PathVariable Integer id,
                                                                          @RequestPart String discountHotel,
                                                                          @RequestPart MultipartFile file
    ) throws JsonProcessingException {
        logger.debug("Request to update discount by id {}", id);
        ObjectMapper mapper = new ObjectMapper();
        DiscountHotelRequestDto discountDto = mapper.readValue(discountHotel, DiscountHotelRequestDto.class);
        SuccessResponseDto<DiscountDto> response = SuccessResponseDto.<DiscountDto>builder()
                .code(DiscountConstant.STATUS_200)
                .message(DiscountConstant.MESSAGE_200)
                .data(discountService.updateDiscount(id,discountDto,file))
                .build();
        return ResponseEntity.ok(response);
    }
    @PostMapping("create")
    public ResponseEntity<SuccessResponseDto<DiscountDto>> createDiscount(
            @RequestPart String discountHotel,
            @RequestPart MultipartFile file
    ) throws JsonProcessingException {
        logger.debug("Request to create discount {}", discountHotel);
        ObjectMapper mapper = new ObjectMapper();
        DiscountHotelRequestDto discountDto = mapper.readValue(discountHotel, DiscountHotelRequestDto.class);
        SuccessResponseDto<DiscountDto> response = SuccessResponseDto.<DiscountDto>builder()
                .code(DiscountConstant.STATUS_200)
                .message(DiscountConstant.MESSAGE_200)
                .data(discountService.createDiscount(discountDto,file))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
