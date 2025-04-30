package com.thuc.bookings.service.client;

import com.thuc.bookings.dto.requestDto.BookingDto;
import com.thuc.bookings.dto.requestDto.BookingRoomTypeDto;
import com.thuc.bookings.dto.responseDto.PaymentResponseDto;
import com.thuc.bookings.dto.responseDto.SuccessResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name="rooms",path = "/api/roomtypes")
public interface RoomsFeignClient {
    @PostMapping("/available-rooms")
    public ResponseEntity<SuccessResponseDto<List<Integer>>> availableRooms(@RequestBody BookingRoomTypeDto bookingDto, @RequestParam int propertyId);
}
