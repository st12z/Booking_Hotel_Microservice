package com.thuc.bookings.service;

import com.thuc.bookings.dto.requestDto.BookingDto;
import com.thuc.bookings.utils.BillStatus;
import jakarta.servlet.http.HttpServletRequest;

public interface IBookingService {

    String confirm(BookingDto bookingDto);

    void updateBillStatus(String billCode,BillStatus billStatus);

    void removeHoldInRedis(String billCode);
}
