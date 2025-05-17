package com.thuc.bookings.service;

import com.thuc.bookings.dto.responseDto.BillDto;
import com.thuc.bookings.dto.responseDto.PageResponseDto;

import java.util.List;

public interface IBillService {

    PageResponseDto<List<BillDto>> getMyBills(String email, Integer pageNo, Integer pageSize);

    PageResponseDto<List<BillDto>> getBillsByKeyword(String email,String keyword,Integer pageNo, Integer pageSize);

    BillDto getBillByBillCode(String billCode);

    Integer getAmountBills();
}