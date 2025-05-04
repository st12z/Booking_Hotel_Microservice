package com.thuc.bookings.converter;

import com.thuc.bookings.dto.requestDto.BookingDto;
import com.thuc.bookings.dto.requestDto.BookingRoomTypeDto;
import com.thuc.bookings.dto.responseDto.BookingRoomsDto;
import com.thuc.bookings.entity.Bill;
import com.thuc.bookings.entity.BookingRooms;

import java.util.List;

public class BookingRoomsConverter {
    public static BookingRooms toBookingRooms(BookingRoomTypeDto bookingDto, int billId,List<Integer> numRooms) {
        BookingRooms bookingRooms = BookingRooms.builder()
                .roomTypeId(bookingDto.getRoomTypeId())
                .quantityRooms(bookingDto.getQuantityRooms())
                .billId(billId)
                .checkIn(bookingDto.getCheckIn())
                .checkOut(bookingDto.getCheckOut())
                .dayStays(bookingDto.getDayStays())
                .originPayment(bookingDto.getOriginPayment())
                .promotion(bookingDto.getPromotion())
                .newPayment(bookingDto.getNewPayment())
                .propertyId(bookingDto.getPropertyId())
                .build();
        bookingRooms.setNumRooms(numRooms);
        return bookingRooms;
    }
    public static BookingRoomsDto toBookingRoomsDto(BookingRooms bookingDto) {
        BookingRoomsDto bookingRooms = BookingRoomsDto.builder()
                .id(bookingDto.getId())
                .billId(bookingDto.getBillId())
                .roomTypeId(bookingDto.getRoomTypeId())
                .quantityRooms(bookingDto.getQuantityRooms())
                .checkIn(bookingDto.getCheckIn())
                .checkOut(bookingDto.getCheckOut())
                .dayStays(bookingDto.getDayStays())
                .originPayment(bookingDto.getOriginPayment())
                .promotion(bookingDto.getPromotion())
                .newPayment(bookingDto.getNewPayment())
                .propertyId(bookingDto.getPropertyId())
                .numRooms(bookingDto.getNumRooms())
                .build();
        return bookingRooms;
    }
}
