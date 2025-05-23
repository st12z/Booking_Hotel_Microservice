package com.thuc.bookings.service;

import com.thuc.bookings.dto.responseDto.RefundBillDto;

public interface IRefundBillService {
    void createRefundBill(RefundBillDto refundBillDto);
}
