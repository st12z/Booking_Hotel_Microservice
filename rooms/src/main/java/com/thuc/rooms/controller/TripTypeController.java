package com.thuc.rooms.controller;

import com.thuc.rooms.constants.TripConstant;
import com.thuc.rooms.dto.SuccessResponseDto;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/triptypes")
public class TripTypeController {
    private final ITripService tripService;
    private final Logger log = LoggerFactory.getLogger(TripTypeController.class);
    @GetMapping("")
    public ResponseEntity<?> getAllTrips() {
        log.debug("Requested to getAllTripTypes");
        SuccessResponseDto response = SuccessResponseDto.builder()
                .message(TripConstant.MESSAGE_200)
                .code(TripConstant.STATUS_200)
                .data(tripService.getAllTripTypes())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
