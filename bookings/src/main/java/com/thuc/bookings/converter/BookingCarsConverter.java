package com.thuc.bookings.converter;

import com.thuc.bookings.dto.requestDto.BookingCarsRequestDto;
import com.thuc.bookings.dto.requestDto.BookingDto;
import com.thuc.bookings.entity.Bill;
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
}
