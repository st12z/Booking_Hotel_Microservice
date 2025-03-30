package com.thuc.rooms.controller;

import com.thuc.rooms.constants.PropertyConstant;
import com.thuc.rooms.dto.SearchDto;
import com.thuc.rooms.dto.SuccessResponseDto;
import com.thuc.rooms.service.ISearchService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {
    private final ISearchService searchService;
    private final Logger log = LoggerFactory.getLogger(SearchController.class);
    @GetMapping("")
    public ResponseEntity<?> search(
            @RequestBody(required = true) SearchDto searchDto,
            @RequestParam(required=true,defaultValue="1") int pageNo,
            @RequestParam(required=true,defaultValue="5") int pageSize
    ) {
        log.debug("Requested to search with {}", searchDto);
        SuccessResponseDto successResponseDto = SuccessResponseDto.builder()
                .code(PropertyConstant.STATUS_200)
                .message(PropertyConstant.MESSAGE_200)
                .data(searchService.getPropertiesBySearchV2(pageNo,pageSize,searchDto))
                .build();
        return ResponseEntity.ok().body(successResponseDto);
    }
}
