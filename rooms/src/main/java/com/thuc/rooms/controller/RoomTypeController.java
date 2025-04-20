package com.thuc.rooms.controller;

import com.thuc.rooms.constants.RoomTypeConstant;
import com.thuc.rooms.dto.CheckRoomDto;
import com.thuc.rooms.dto.SearchDto;
import com.thuc.rooms.dto.SuccessResponseDto;
import com.thuc.rooms.service.IRoomTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/roomtypes")
@RequiredArgsConstructor
public class RoomTypeController {
    private final IRoomTypeService roomTypeService;
    private final Logger log = LoggerFactory.getLogger(RoomTypeController.class);
    @GetMapping("")
    public ResponseEntity<?> getRoomTypes(@RequestParam String slugProperty) {
        log.debug("Request to get RoomTypes slugProperty with {}", slugProperty);
        SuccessResponseDto response = SuccessResponseDto.builder()
                .code(RoomTypeConstant.STATUS_200)
                .message(RoomTypeConstant.MESSAGE_200)
                .data(roomTypeService.getAllRoomTypes(slugProperty))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @PostMapping("")
    public ResponseEntity<?> getRoomTypesBySearch(
            @RequestParam String slugProperty,
            @RequestBody SearchDto searchDto) {
        log.debug("Request to search room types with : {}", searchDto);
        SuccessResponseDto response = SuccessResponseDto.builder()
                .code(RoomTypeConstant.STATUS_200)
                .message(RoomTypeConstant.MESSAGE_200)
                .data(roomTypeService.getAllRoomTypesBySearch(slugProperty,searchDto))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    // dung apache bench test api dong thoi
    @PostMapping("/check-room")
    public ResponseEntity<?> checkEnoughRooms(@RequestBody @Valid CheckRoomDto checkRoomDto) {
        log.debug("Request to check enough rooms with {}", checkRoomDto);
        SuccessResponseDto response = SuccessResponseDto.builder()
                .code(RoomTypeConstant.STATUS_200)
                .message(RoomTypeConstant.MESSAGE_200)
                .data(roomTypeService.checkEnoughRooms(checkRoomDto))
                .build();
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getRoomTypeById(@PathVariable Integer id) {
        log.debug("Request to get RoomType by id {}", id);
        SuccessResponseDto response = SuccessResponseDto.builder()
                .code(RoomTypeConstant.STATUS_200)
                .message(RoomTypeConstant.MESSAGE_200)
                .data(roomTypeService.getRoomTypeById(id))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
