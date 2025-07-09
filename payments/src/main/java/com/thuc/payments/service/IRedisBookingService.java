package com.thuc.payments.service;

import com.thuc.payments.dto.BookingDto;

public interface IRedisBookingService {
    public void saveData(String key, BookingDto bookingDto);
    public BookingDto getData(String key);
    public void deleteData(String key);
}
