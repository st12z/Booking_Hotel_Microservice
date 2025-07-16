package com.thuc.rooms.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thuc.rooms.constants.TripConstant;
import com.thuc.rooms.dto.PageResponseDto;
import com.thuc.rooms.dto.SuccessResponseDto;
import com.thuc.rooms.dto.TripDto;
import com.thuc.rooms.dto.TripRequestDto;
import com.thuc.rooms.service.ITripService;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.triangulate.tri.Tri;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @GetMapping("{id}")
    public ResponseEntity<SuccessResponseDto<TripDto>> getTripById(@PathVariable Integer id) {
        log.debug("Requested to getTrip by id {}", id);
        SuccessResponseDto<TripDto> response = SuccessResponseDto.<TripDto>builder()
                .message(TripConstant.MESSAGE_200)
                .code(TripConstant.STATUS_200)
                .data(tripService.getTripById(id))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @GetMapping("/trips-page")
    public ResponseEntity<SuccessResponseDto<PageResponseDto<List<TripDto>>>> getAllTripsPage(
            @RequestParam(required = false,defaultValue = "") String keyword,
            @RequestParam(required = false,defaultValue = "1") Integer pageNo,
            @RequestParam(required = false,defaultValue = "10") Integer pageSize
    ){
        log.debug("Requested to getAllTripsPage with keyword {} pageNo {} pageSize {}", keyword, pageNo, pageSize);
        SuccessResponseDto<PageResponseDto<List<TripDto>>> response = SuccessResponseDto.<PageResponseDto<List<TripDto>>>builder()
                .message(TripConstant.MESSAGE_200)
                .code(TripConstant.STATUS_200)
                .data(tripService.getAllTripsByPage(keyword,pageNo,pageSize))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @PostMapping("/update/{id}")
    public ResponseEntity<SuccessResponseDto<TripDto>> updateTrip(@PathVariable Integer id,
                                                                  @RequestPart String trip,
                                                                  @RequestPart MultipartFile file) throws JsonProcessingException {
        log.debug("Requested to update trip with id {}, tripDto {}", id,trip);
        ObjectMapper mapper = new ObjectMapper();
        TripRequestDto tripDto = mapper.readValue(trip, TripRequestDto.class);
        log.debug("Requested to update trip with id {}, tripDto {}", id, tripDto.toString());
        SuccessResponseDto<TripDto> response = SuccessResponseDto.<TripDto>builder()
                .message(TripConstant.MESSAGE_200)
                .code(TripConstant.STATUS_200)
                .data(tripService.updateTrip(id,tripDto,file))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @PostMapping("/create")
    public ResponseEntity<SuccessResponseDto<TripDto>> createTrip(@RequestPart String trip, @RequestPart MultipartFile file) throws JsonProcessingException {
        log.debug("Requested to create trip with trip {}", trip);
        ObjectMapper mapper = new ObjectMapper();
        TripRequestDto tripDto = mapper.readValue(trip, TripRequestDto.class);
        log.debug("Requested to create trip with trip {}", tripDto.toString());
        SuccessResponseDto<TripDto> response = SuccessResponseDto.<TripDto>builder()
                .message(TripConstant.MESSAGE_200)
                .code(TripConstant.STATUS_200)
                .data(tripService.createTrip(tripDto,file))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
