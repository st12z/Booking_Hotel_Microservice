package com.thuc.bookings.service;

import com.thuc.bookings.dto.responseDto.BillDto;
import com.thuc.bookings.dto.responseDto.PageResponseDto;
import com.thuc.bookings.dto.responseDto.StatisticBillByMonth;

import java.util.List;

public interface IBillService {

    PageResponseDto<List<BillDto>> getMyBills(String email, Integer pageNo, Integer pageSize,String keyword);


    BillDto getBillByBillCode(String billCode);

    Integer getAmountBillsToday();

    Integer getAmountRevenueToday();

    List<StatisticBillByMonth> getAmountBillsByMonth(Integer month);

    List<StatisticBillByMonth> getAmountRevenueByMonth(Integer month);

    List<BillDto> getAllBillsRecently();
}