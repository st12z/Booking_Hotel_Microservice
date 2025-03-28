package com.thuc.rooms.controller;

import com.thuc.rooms.constants.RoomTypeConstant;
import com.thuc.rooms.dto.SuccessResponseDto;
import com.thuc.rooms.service.IRoomTypeService;
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
@RequestMapping("/api/roomtypes")
@RequiredArgsConstructor
public class RoomTypeController {
    private final IRoomTypeService roomTypeService;
    private final Logger log = LoggerFactory.getLogger(RoomTypeController.class);
    @GetMapping("")
    public ResponseEntity<?> getRoomTypes(@RequestParam int propertyId) {
        log.debug("Request to get RoomTypes : {}", propertyId);
        SuccessResponseDto response = SuccessResponseDto.builder()
                .code(RoomTypeConstant.STATUS_200)
                .message(RoomTypeConstant.MESSAGE_200)
                .data(roomTypeService.getAllRoomTypes(propertyId))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
