package com.thuc.payments.service;


import com.thuc.payments.dto.BookingDto;
import com.thuc.payments.dto.PaymentResponseDto;
import jakarta.servlet.http.HttpServletRequest;

public interface IPaymentService {
    public PaymentResponseDto getUrlPayment(HttpServletRequest request, BookingDto bookingDto);
}
