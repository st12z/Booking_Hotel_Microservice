package com.thuc.bookings.service;

import com.thuc.bookings.dto.requestDto.FilterBillsDto;
import com.thuc.bookings.dto.responseDto.BillDto;
import com.thuc.bookings.dto.responseDto.PageResponseDto;
import com.thuc.bookings.dto.responseDto.StatisticBillByMonth;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface IBillService {

    PageResponseDto<List<BillDto>> getMyBills(String email, Integer pageNo, Integer pageSize,String keyword);


    BillDto getBillByBillCode(String billCode);

    Integer getAmountBillsToday();

    Integer getAmountRevenueToday();

    List<StatisticBillByMonth> getAmountBillsByMonth(Integer month);

    List<StatisticBillByMonth> getAmountRevenueByMonth(Integer month);

    List<BillDto> getAllBillsRecently();

    Map<Integer,Integer> getAmountBillsByPropertyIds(List<Integer> propertyIds);

    Map<Integer,Integer> getAmountRevenueByPropertyIds(List<Integer> propertyIds);

    PageResponseDto<List<BillDto>> getAllBills(FilterBillsDto filterBillsDto) throws ParseException;

    List<String> getAllTypeOfBillStatus();

    PageResponseDto<List<BillDto>> getBillsByKeyword(String keyword, Integer pageNo, Integer pageSize);
}