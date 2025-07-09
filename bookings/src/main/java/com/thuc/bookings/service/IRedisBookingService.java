package com.thuc.bookings.service;

import com.thuc.bookings.dto.responseDto.BookingDto;

public interface IRedisBookingService {
    public void saveData(String key, BookingDto bookingDto);
    public BookingDto getData(String key);
    public void deleteData(String key);
}
