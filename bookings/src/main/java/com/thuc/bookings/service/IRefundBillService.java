package com.thuc.bookings.service;

import com.thuc.bookings.dto.requestDto.FilterRefundBillDto;
import com.thuc.bookings.dto.responseDto.PageResponseDto;
import com.thuc.bookings.dto.responseDto.RefundBillDto;

import java.text.ParseException;
import java.util.List;

public interface IRefundBillService {
    void createRefundBill(RefundBillDto refundBillDto);

    PageResponseDto<List<RefundBillDto>> getAllRefundBills(FilterRefundBillDto filterDto) throws ParseException;

    PageResponseDto<List<RefundBillDto>> getRefundBillsBySearch(String keyword, Integer pageNo, Integer pageSize);
}
