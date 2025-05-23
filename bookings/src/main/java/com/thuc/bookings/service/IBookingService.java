package com.thuc.bookings.service;

import com.thuc.bookings.dto.requestDto.BookingDto;
import com.thuc.bookings.dto.responseDto.BookingCarsResponseDto;
import com.thuc.bookings.dto.responseDto.BookingRoomsDto;
import com.thuc.bookings.dto.responseDto.RefundBillDto;
import com.thuc.bookings.utils.BillStatus;

import java.util.List;

public interface IBookingService {

    String confirm(BookingDto bookingDto);

    void updateBillStatus(String billCode,BillStatus billStatus);

    void removeHoldInRedis(String billCode);


    List<BookingRoomsDto> getListBookingRooms(Integer roomTypeId,Integer propertyId);

    List<BookingRoomsDto> getListBookingRoomsByBillId(Integer billId);

    List<BookingCarsResponseDto> getListBookingCarsByBillId(Integer billId);


}
