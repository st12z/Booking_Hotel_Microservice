package com.thuc.rooms.controller;

import com.thuc.rooms.constants.TripConstant;
import com.thuc.rooms.dto.SuccessResponseDto;
import com.thuc.rooms.service.ITripService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/destinations")
public class DestinationController {
    private final ITripService tripService;
    private final Logger log = LoggerFactory.getLogger(DestinationController.class);
    @GetMapping("")
    public ResponseEntity<?> getDestinationsBySearch(@RequestParam(required = false,defaultValue = "") String keyword) {
        log.debug("Requested to getDestinationsBySearch with search {}", keyword);
        SuccessResponseDto response = SuccessResponseDto.builder()
                .message(TripConstant.MESSAGE_200)
                .code(TripConstant.STATUS_200)
                .data(tripService.getDestinationsBySearch(keyword))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @PostMapping
    public ResponseEntity<?> getDestinationsBySuggest(@RequestBody List<String> destinations){
        log.debug("Requested to getDestinationsBySuggest with destinations {}", destinations);
        SuccessResponseDto response = SuccessResponseDto.builder()
                .message("Get destinations by suggest successfully")
                .code(200)
                .data(tripService.getDestinationsBySuggest(destinations))
                .build();
        return ResponseEntity.ok(response);
    }

}
