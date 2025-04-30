package com.thuc.rooms.controller;

import com.thuc.rooms.constants.TripConstant;
import com.thuc.rooms.dto.SuccessResponseDto;
import com.thuc.rooms.dto.TripDto;
import com.thuc.rooms.service.ITripService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/trips")
public class TripController {
    private final ITripService tripService;
    private final Logger log = LoggerFactory.getLogger(TripController.class);
    @GetMapping("")
    public ResponseEntity<SuccessResponseDto<List<TripDto>>> getAllTrips(@RequestParam(required = false,defaultValue = "") String trip) {
        log.debug("Requested to getAllTrips with trip {}", trip);
        SuccessResponseDto<List<TripDto>> response = SuccessResponseDto
                .<List<TripDto>>builder()
                .message(TripConstant.MESSAGE_200)
                .code(TripConstant.STATUS_200)
                .data(tripService.getAllTrips(trip))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
