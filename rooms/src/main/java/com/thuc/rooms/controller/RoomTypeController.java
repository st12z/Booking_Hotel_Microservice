package com.thuc.rooms.controller;

import com.thuc.rooms.constants.RoomTypeConstant;
import com.thuc.rooms.dto.*;
import com.thuc.rooms.entity.Room;
import com.thuc.rooms.service.IRoomTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roomtypes")
@RequiredArgsConstructor
public class RoomTypeController {
    private final IRoomTypeService roomTypeService;
    private final Logger log = LoggerFactory.getLogger(RoomTypeController.class);
    @GetMapping("")
    public ResponseEntity<SuccessResponseDto<List<RoomTypeDto>>> getRoomTypesBySlug(@RequestParam String slugProperty) {
        log.debug("Request to get RoomTypes slugProperty with {}", slugProperty);
        SuccessResponseDto<List<RoomTypeDto>> response = SuccessResponseDto.<List<RoomTypeDto>>builder()
                .code(RoomTypeConstant.STATUS_200)
                .message(RoomTypeConstant.MESSAGE_200)
                .data(roomTypeService.getAllRoomTypesBySlug(slugProperty))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @GetMapping("/all")
    public ResponseEntity<SuccessResponseDto<List<RoomTypeDto>>> getAllRoomTypes(){
        log.debug("Request to get all roomtypes");
        SuccessResponseDto<List<RoomTypeDto>> response = SuccessResponseDto.<List<RoomTypeDto>>builder()
                .code(RoomTypeConstant.STATUS_200)
                .message(RoomTypeConstant.MESSAGE_200)
                .data(roomTypeService.getAllRoomTypes())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @PostMapping("")
    public ResponseEntity<SuccessResponseDto<List<RoomTypeDto>>> getRoomTypesBySearch(
            @RequestParam String slugProperty,
            @RequestBody SearchDto searchDto) {
        log.debug("Request to search room types with : {}", searchDto);
        SuccessResponseDto<List<RoomTypeDto>> response = SuccessResponseDto.<List<RoomTypeDto>>builder()
                .code(RoomTypeConstant.STATUS_200)
                .message(RoomTypeConstant.MESSAGE_200)
                .data(roomTypeService.getAllRoomTypesBySearch(slugProperty,searchDto))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    // test api concurrency by python
    @PostMapping("/check-room")
    public ResponseEntity<SuccessResponseDto<Integer >> checkEnoughRooms(@RequestBody @Valid CheckRoomDto checkRoomDto) {
        log.debug("Request to check enough rooms with {}", checkRoomDto);
        SuccessResponseDto<Integer > response = SuccessResponseDto.<Integer >builder()
                .code(RoomTypeConstant.STATUS_200)
                .message(RoomTypeConstant.MESSAGE_200)
                .data(roomTypeService.checkEnoughRooms(checkRoomDto))
                .build();
        return ResponseEntity.ok(response);
    }
    // hold room
    @PostMapping("/hold-rooms")
    public ResponseEntity<SuccessResponseDto<Boolean >> holdRooms(@RequestBody List<CheckRoomDto> roomReversed){
        log.debug("Request to hold rooms with {}", roomReversed);
        boolean check = roomTypeService.holdRooms(roomReversed);
        SuccessResponseDto<Boolean > response = SuccessResponseDto
                .<Boolean >builder()
                .data(check)
                .build();
        if(check){
            response.setCode(RoomTypeConstant.STATUS_200);
            response.setMessage(RoomTypeConstant.MESSAGE_200);
            return ResponseEntity.status(HttpStatus.OK).body(response);

        }
        else{
            response.setCode(RoomTypeConstant.STATUS_400);
            response.setMessage(RoomTypeConstant.MESSAGE_400);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponseDto<RoomTypeDto>> getRoomTypeById(@PathVariable Integer id) {
        log.debug("Request to get RoomType by id {}", id);
        SuccessResponseDto<RoomTypeDto> response = SuccessResponseDto.<RoomTypeDto>builder()
                .code(RoomTypeConstant.STATUS_200)
                .message(RoomTypeConstant.MESSAGE_200)
                .data(roomTypeService.getRoomTypeById(id))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @PostMapping("/check-bookrooms")
    public ResponseEntity<?> checkHoldRooms(@RequestBody @Valid BookingDto bookingDto) {
        log.debug("Request to check hold rooms with {}", bookingDto);
        boolean check = roomTypeService.checkHoldRooms(bookingDto);
        String message =check  ?
                "Bạn có thể đặt phòng!" :"Vui lòng kiểm tra lại!";
        log.debug("message = {}", message);
        int code = check ? RoomTypeConstant.STATUS_200 : RoomTypeConstant.STATUS_400;
        HttpStatus status = check ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        SuccessResponseDto<?> response = SuccessResponseDto.builder()
                .code(code)
                .message(message)
                .build();
        log.debug("response = {}", response);
        return ResponseEntity.status(status).body(response);
    }
    @PostMapping("/available-rooms")
    public ResponseEntity<SuccessResponseDto<List<Integer>>> availableRooms(@RequestBody @Valid BookingRoomTypeDto bookingRoomTypeDto,
                                            @RequestParam Integer propertyId) {
        SuccessResponseDto<List<Integer>> response = SuccessResponseDto
                .<List<Integer>>builder()
                .code(RoomTypeConstant.STATUS_200)
                .message(RoomTypeConstant.MESSAGE_200)
                .data(roomTypeService.getAvailableRooms(bookingRoomTypeDto,propertyId))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @PostMapping("/confirm-booking")
    public ResponseEntity<SuccessResponseDto<?>> confirmBookingRooms(@RequestBody @Valid BookingRoomConfirmDto bookingRoomConfirmDto,
                                                                     @RequestParam(value = "discountCarId", required = false) Integer discountCarId,
                                                                     @RequestParam(value = "discountHotelId", required = false) Integer discountHotelId

    ) {
        log.debug("Request to confirm booking rooms with {}", bookingRoomConfirmDto.toString());
        SuccessResponseDto<?> response = SuccessResponseDto.builder()
                .code(RoomTypeConstant.STATUS_200)
                .message(RoomTypeConstant.MESSAGE_200)
                .data(roomTypeService.confirmBooking(bookingRoomConfirmDto,discountCarId,discountHotelId))
                .build();
        return  ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @PostMapping("/create")
    public ResponseEntity<SuccessResponseDto<RoomTypeDto>> createRoomType(@RequestBody @Valid RoomTypeRequestDto roomTypeDto) {
        log.debug("Request to create room type with {}", roomTypeDto);
        SuccessResponseDto<RoomTypeDto> response = SuccessResponseDto.<RoomTypeDto>builder()
                .code(RoomTypeConstant.STATUS_200)
                .message(RoomTypeConstant.MESSAGE_200)
                .data(roomTypeService.createRoomType(roomTypeDto))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @PostMapping("/update/{id}")
    public ResponseEntity<SuccessResponseDto<RoomTypeDto>> updateRoomType(@PathVariable Integer id,@RequestBody RoomTypeRequestDto roomTypeDto) {
        log.debug("Request to update room type with {}", roomTypeDto);
        SuccessResponseDto<RoomTypeDto> response = SuccessResponseDto.<RoomTypeDto>builder()
                .code(RoomTypeConstant.STATUS_200)
                .message(RoomTypeConstant.MESSAGE_200)
                .data(roomTypeService.updateRoomType(id,roomTypeDto))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
