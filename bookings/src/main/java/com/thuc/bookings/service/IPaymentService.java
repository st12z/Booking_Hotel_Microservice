package com.thuc.bookings.service;


import com.thuc.bookings.dto.requestDto.BookingDto;
import jakarta.servlet.http.HttpServletRequest;

public interface IPaymentService {
    public String getUrlPayment(HttpServletRequest request, BookingDto bookingDto);
}
