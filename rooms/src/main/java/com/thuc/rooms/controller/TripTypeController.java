package com.thuc.rooms.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thuc.rooms.constants.TripConstant;
import com.thuc.rooms.dto.PageResponseDto;
import com.thuc.rooms.dto.SuccessResponseDto;
import com.thuc.rooms.dto.TripTypeDto;
import com.thuc.rooms.dto.TripTypeRequestDto;
import com.thuc.rooms.service.ITripService;
import com.thuc.rooms.service.ITripTypeService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/triptypes")
public class TripTypeController {
    private final ITripService tripService;
    private final Logger log = LoggerFactory.getLogger(TripTypeController.class);
    private final ITripTypeService tripTypeService;
    @GetMapping("")
    public ResponseEntity<SuccessResponseDto<List<TripTypeDto>>> getAllTrips() {
        log.debug("Requested to getAllTripTypes");
        SuccessResponseDto<List<TripTypeDto>> response = SuccessResponseDto
                .<List<TripTypeDto>>builder()
                .message(TripConstant.MESSAGE_200)
                .code(TripConstant.STATUS_200)
                .data(tripService.getAllTripTypes())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @GetMapping("search")
    public ResponseEntity<SuccessResponseDto<PageResponseDto<List<TripTypeDto>>>> getSearchTripTypes(
            @RequestParam(defaultValue = "",required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize
    ){
        log.debug("Requested to getSearchTripTypes with keyword: {} pageNo: {}, pageSize: {}",keyword, pageNo, pageSize);
        SuccessResponseDto<PageResponseDto<List<TripTypeDto>>> response = SuccessResponseDto.<PageResponseDto<List<TripTypeDto>>>builder()
                .message(TripConstant.MESSAGE_200)
                .code(TripConstant.STATUS_200)
                .data(tripTypeService.getSearchTripTypes(keyword,pageNo,pageSize))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }
    @GetMapping("{id}")
    public ResponseEntity<SuccessResponseDto<TripTypeDto>> getTripTypeById(@PathVariable Integer id) {
        log.debug("Requested to getTripType with id: {}", id);
        SuccessResponseDto<TripTypeDto> response = SuccessResponseDto.<TripTypeDto>builder()
                .message(TripConstant.MESSAGE_200)
                .code(TripConstant.STATUS_200)
                .data(tripTypeService.getTripTypeById(id))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @PostMapping("/update/{id}")
    public ResponseEntity<SuccessResponseDto<TripTypeDto>> updateTripTypeId(@PathVariable Integer id,
                                                                            @RequestPart String tripType,
                                                                            @RequestPart MultipartFile file
    ) throws JsonProcessingException {
        log.debug("Requested to update tripType with id: {} tripType: {}", id, tripType);
        ObjectMapper objectMapper = new ObjectMapper();
        TripTypeRequestDto tripTypeDto = objectMapper.readValue(tripType,TripTypeRequestDto.class);
        SuccessResponseDto<TripTypeDto> response = SuccessResponseDto.<TripTypeDto>builder()
                .message(TripConstant.MESSAGE_200)
                .code(TripConstant.STATUS_200)
                .data(tripTypeService.updateTripType(id,tripTypeDto,file))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @PostMapping("/create")
    public ResponseEntity<SuccessResponseDto<TripTypeDto>> createTripType(@RequestPart String tripType,@RequestPart MultipartFile file) throws JsonProcessingException {
        log.debug("Requested to create tripType with tripType: {}", tripType);
        ObjectMapper objectMapper = new ObjectMapper();
        TripTypeRequestDto tripTypeDto = objectMapper.readValue(tripType,TripTypeRequestDto.class);
        SuccessResponseDto<TripTypeDto> response = SuccessResponseDto.<TripTypeDto>builder()
                .message(TripConstant.MESSAGE_200)
                .code(TripConstant.STATUS_200)
                .data(tripTypeService.createTripType(tripTypeDto,file))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
