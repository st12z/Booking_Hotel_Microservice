package com.thuc.rooms.controller;

import com.thuc.rooms.constants.RoomConstant;
import com.thuc.rooms.constants.RoomTypeConstant;
import com.thuc.rooms.dto.RoomDto;
import com.thuc.rooms.dto.RoomRequestDto;
import com.thuc.rooms.dto.SuccessResponseDto;
import com.thuc.rooms.service.IRoomService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomsController {
    private final Logger logger =  LoggerFactory.getLogger(this.getClass());
    private final IRoomService roomService;
    @PostMapping("/create")
    public ResponseEntity<SuccessResponseDto<RoomDto>> createRoom(@RequestBody RoomRequestDto roomDto) {
        logger.debug("Creating a room : {}", roomDto);
        SuccessResponseDto<RoomDto> response = SuccessResponseDto.<RoomDto>builder()
                .code(RoomConstant.STATUS_200)
                .message(RoomConstant.MESSAGE_200)
                .data(roomService.createRoom(roomDto))
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<SuccessResponseDto<Integer>> deleteRoom(@PathVariable Integer id) {
        logger.debug("Deleting a room : {}", id);
        SuccessResponseDto<Integer> response = SuccessResponseDto.<Integer>builder()
                .code(RoomConstant.STATUS_200)
                .message(RoomConstant.MESSAGE_200)
                .data(roomService.deleteRoom(id))
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/quantity-rooms")
    public ResponseEntity<SuccessResponseDto<Integer>> getQuantityRooms(@RequestParam Integer propertyId, @RequestParam Integer roomTypeId) {
        logger.debug("propertyId: {}, roomTypeId: {}", propertyId, roomTypeId);
        SuccessResponseDto<Integer> response = SuccessResponseDto.<Integer>builder()
                .code(RoomConstant.STATUS_200)
                .message(RoomConstant.MESSAGE_200)
                .data(roomService.getQuantityRoomsByPropertyIdAndRoomTypeId(propertyId, roomTypeId))
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
