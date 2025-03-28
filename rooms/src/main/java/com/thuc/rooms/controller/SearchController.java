package com.thuc.rooms.controller;

import com.thuc.rooms.dto.SearchDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/search")
public class SearchController {
    private final Logger log = LoggerFactory.getLogger(SearchController.class);
    @GetMapping("")
    public ResponseEntity<String> search(@RequestBody(required = true) SearchDto searchDto) {
        log.debug("search with {}", searchDto);
        return ResponseEntity.ok().body(searchDto.toString());
    }
}
