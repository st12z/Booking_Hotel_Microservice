package com.thuc.rooms.controller;

import com.thuc.rooms.constants.PropertyConstant;
import com.thuc.rooms.dto.*;
import com.thuc.rooms.service.IFilterService;
import com.thuc.rooms.service.ISearchService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Filter;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {
    private final ISearchService searchService;
    private final IFilterService filterService;
    private final Logger log = LoggerFactory.getLogger(SearchController.class);
    @GetMapping("")
    public ResponseEntity<SuccessResponseDto<PageResponseDto<List<PropertyDto>>>> search(
            @RequestParam String destination,
            @RequestParam(required = false)  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime checkIn,
            @RequestParam(required = false)  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime checkOut,
            @RequestParam(required = false)  Integer quantityBeds,
            @RequestParam(defaultValue="1") int pageNo,
            @RequestParam(defaultValue="10") int pageSize
    ) {
        SearchDto searchDto = new SearchDto(destination, checkIn, checkOut, quantityBeds);
        log.debug("Requested to search with {}", searchDto);
        SuccessResponseDto<PageResponseDto<List<PropertyDto>>> successResponseDto = SuccessResponseDto
                .<PageResponseDto<List<PropertyDto>>>builder()
                .code(PropertyConstant.STATUS_200)
                .message(PropertyConstant.MESSAGE_200)
                .data(searchService.getPropertiesBySearchV1(pageNo,pageSize,searchDto))
                .build();
        return ResponseEntity.ok().body(successResponseDto);
    }
    @PostMapping("")
    public ResponseEntity<SuccessResponseDto<PageResponseDto<List<PropertyDto>>>> filter(
            @RequestParam String destination,
            @RequestParam(required = false)  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime checkIn,
            @RequestParam(required = false)  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime checkOut,
            @RequestParam(required = false)  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Integer quantityBeds,
            @RequestParam(defaultValue="1") int pageNo,
            @RequestParam(defaultValue="10") int pageSize,
            @RequestBody FilterDto filter
    ){
        SearchDto searchDto = new SearchDto(destination, checkIn, checkOut, quantityBeds);
        log.debug("Requested to filter with search:{} and filter:{}",searchDto, filter);
        SuccessResponseDto<PageResponseDto<List<PropertyDto>>> successResponseDto = SuccessResponseDto
                .<PageResponseDto<List<PropertyDto>>>builder()
                .code(PropertyConstant.STATUS_200)
                .message(PropertyConstant.MESSAGE_200)
                .data(filterService.filterByCondition(searchDto,filter,pageNo,pageSize))
                .build();
        return ResponseEntity.ok().body(successResponseDto);
    }

}
