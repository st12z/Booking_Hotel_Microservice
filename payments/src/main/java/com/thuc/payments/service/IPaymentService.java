package com.thuc.payments.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.thuc.payments.dto.*;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface IPaymentService {
    public PaymentResponseDto getUrlPayment(HttpServletRequest request, BookingDto bookingDto);

    VnpayRefundResponseDto refund(HttpServletRequest request, String billCode) throws JsonProcessingException;

    List<StatisticTransactionDto> getAmountTransactionMonth(FilterStatistic filterDto);

    List<StatisticTransactionDto> getRevenueTransactionByMonth(FilterStatistic filterDto);

    List<StatisticTransactionTypeDto> getStatisticTransactionType(Integer month);
}
