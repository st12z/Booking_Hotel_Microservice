package com.thuc.bookings.service;

import com.thuc.bookings.dto.responseDto.BookingCarsResponseDto;
import com.thuc.bookings.dto.responseDto.BookingRoomsDto;
import com.thuc.bookings.utils.BillStatus;

import java.util.List;

public interface IBookingService {

    String confirm(String uniqueCheck);

    void updateBillStatus(String billCode,BillStatus billStatus);

    void removeHoldInRedis(String billCode);


    List<BookingRoomsDto> getListBookingRooms(Integer roomTypeId,Integer propertyId);

    List<BookingRoomsDto> getListBookingRoomsByBillId(Integer billId);

    List<BookingCarsResponseDto> getListBookingCarsByBillId(Integer billId);


}
