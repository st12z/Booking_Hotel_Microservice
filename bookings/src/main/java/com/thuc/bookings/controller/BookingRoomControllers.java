package com.thuc.bookings.controller;

import com.thuc.bookings.constants.BookingConstant;
import com.thuc.bookings.dto.responseDto.BookingRoomsDto;
import com.thuc.bookings.dto.responseDto.SuccessResponseDto;
import com.thuc.bookings.service.IBookingService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookingrooms")
@RequiredArgsConstructor
public class BookingRoomControllers {
    private final Logger log = LoggerFactory.getLogger(BookingRoomControllers.class);
    private final IBookingService bookingService;
    @GetMapping("")
    public ResponseEntity<SuccessResponseDto<List<BookingRoomsDto>>> getBookingRooms(@RequestParam(required = false) Integer roomTypeId,
                                                                                     @RequestParam Integer propertyId
    ){
        log.debug("getAvailableBookingRooms with roomTypeId {} and propertyId {}", roomTypeId, propertyId);

        SuccessResponseDto<List<BookingRoomsDto>> response = SuccessResponseDto.<List<BookingRoomsDto>>builder()
                .code(BookingConstant.STATUS_200)
                .message(BookingConstant.MESSAGE_200)
                .data(bookingService.getListBookingRooms(roomTypeId,propertyId))
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("{billId}")
    public ResponseEntity<SuccessResponseDto<List<BookingRoomsDto>>> getBookingRoomsByBillId(@PathVariable Integer billId) {
        log.debug("getBookingRoomsByBillId with billId {}", billId);
        SuccessResponseDto<List<BookingRoomsDto>> response = SuccessResponseDto.<List<BookingRoomsDto>>builder()
                .code(BookingConstant.STATUS_200)
                .message(BookingConstant.MESSAGE_200)
                .data(bookingService.getListBookingRoomsByBillId(billId))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
