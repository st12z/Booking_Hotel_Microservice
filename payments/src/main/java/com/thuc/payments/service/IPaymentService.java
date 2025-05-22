package com.thuc.payments.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.thuc.payments.dto.BookingDto;
import com.thuc.payments.dto.PaymentResponseDto;
import com.thuc.payments.dto.VnpayRefundResponseDto;
import jakarta.servlet.http.HttpServletRequest;

public interface IPaymentService {
    public PaymentResponseDto getUrlPayment(HttpServletRequest request, BookingDto bookingDto);

    VnpayRefundResponseDto refund(HttpServletRequest request, String billCode) throws JsonProcessingException;
}
