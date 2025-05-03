package com.thuc.rooms.service.client;

import com.thuc.rooms.dto.BookingRoomsDto;
import com.thuc.rooms.dto.SuccessResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "bookings",path = "/api/bookingrooms")
public interface BookingsFeignClient {
    @GetMapping("")
    public ResponseEntity<SuccessResponseDto<List<BookingRoomsDto>>> getBookingRooms(@RequestParam(required = false) Integer roomTypeId,
                                                                                     @RequestParam Integer propertyId
    );

}
