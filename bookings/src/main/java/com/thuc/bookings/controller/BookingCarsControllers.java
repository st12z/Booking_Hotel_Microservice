package com.thuc.bookings.controller;

import com.thuc.bookings.constants.BookingConstant;
import com.thuc.bookings.dto.responseDto.BookingCarsResponseDto;
import com.thuc.bookings.dto.responseDto.SuccessResponseDto;
import com.thuc.bookings.service.IBookingService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/bookingcars")
@RequiredArgsConstructor
public class BookingCarsControllers {
    private final IBookingService bookingService;
    private final Logger log = LoggerFactory.getLogger(BookingCarsControllers.class);
    @GetMapping("{billId}")
    public ResponseEntity<SuccessResponseDto<List<BookingCarsResponseDto>>> getBookingCars(@PathVariable("billId") Integer billId) {
        log.debug("getBookingCars billId={}", billId);
        SuccessResponseDto<List<BookingCarsResponseDto>> response = SuccessResponseDto.<List<BookingCarsResponseDto>>builder()
                .code(BookingConstant.STATUS_200)
                .message(BookingConstant.MESSAGE_200)
                .data(bookingService.getListBookingCarsByBillId(billId))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
