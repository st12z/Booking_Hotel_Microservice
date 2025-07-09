package com.thuc.bookings.converter;

import com.thuc.bookings.dto.requestDto.BookingCarsRequestDto;
import com.thuc.bookings.dto.responseDto.BookingCarsResponseDto;
import com.thuc.bookings.dto.responseDto.VehicleDto;
import com.thuc.bookings.entity.BookingCars;

public class BookingCarsConverter {
    public static BookingCars toBookingCars(BookingCarsRequestDto bookingDto, int billId) {
        BookingCars bookingCars = BookingCars.builder()
                .billId(billId)
                .vehicleId(bookingDto.getId())
                .priceBooking(bookingDto.getPriceBooking())
                .build();
        return bookingCars;
    }
    public static BookingCarsResponseDto toBookingCarsResponseDto(BookingCars bookingCars, VehicleDto vehicle) {
        return BookingCarsResponseDto.builder()
                .id(bookingCars.getId())
                .vehicle(vehicle)
                .billId(bookingCars.getBillId())
                .priceBooking(bookingCars.getPriceBooking())
                .build();
    }
}
