package com.thuc.bookings.service.client;

import com.thuc.bookings.dto.requestDto.BookingRoomTypeDto;
import com.thuc.bookings.dto.responseDto.BookingRoomConfirmDto;
import com.thuc.bookings.dto.responseDto.RoomTypeDto;
import com.thuc.bookings.dto.responseDto.SuccessResponseDto;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name="rooms", contextId = "roomTypesClient",path = "/api/roomtypes")
public interface RoomTypesFeignClient {
    @PostMapping("/available-rooms")
    public ResponseEntity<SuccessResponseDto<List<Integer>>> availableRooms(@RequestBody BookingRoomTypeDto bookingDto, @RequestParam int propertyId);
    @PostMapping("/confirm-booking")
    public ResponseEntity<SuccessResponseDto<?>> confirmBookingRooms(@RequestBody @Valid BookingRoomConfirmDto bookingRoomConfirmDto,
                                                                     @RequestParam(value = "discountCarId", required = false) Integer discountCarId,
                                                                     @RequestParam(value = "discountHotelId", required = false) Integer discountHotelId
    );
    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponseDto<RoomTypeDto>> getRoomTypeById(@PathVariable Integer id) ;
}
