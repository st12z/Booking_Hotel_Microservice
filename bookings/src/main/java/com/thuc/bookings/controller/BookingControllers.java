package com.thuc.bookings.controller;

import com.thuc.bookings.constants.BookingConstant;
import com.thuc.bookings.dto.requestDto.BookingDto;
import com.thuc.bookings.dto.responseDto.SuccessResponseDto;
import com.thuc.bookings.service.IBookingService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingControllers {
    private final Logger log = LoggerFactory.getLogger(BookingControllers.class);
    private final IBookingService bookingService;
    @PostMapping("/confirm")
    public ResponseEntity<SuccessResponseDto<String>> confirm(@RequestBody BookingDto bookingDto) {
        log.debug("bookingDto: {}", bookingDto);
        SuccessResponseDto<String> response = SuccessResponseDto.<String>builder()
                .code(BookingConstant.STATUS_200)
                .message(BookingConstant.MESSAGE_200)
                .data(bookingService.confirm(bookingDto))
                .build();
        return ResponseEntity.ok(response);
    }
}
