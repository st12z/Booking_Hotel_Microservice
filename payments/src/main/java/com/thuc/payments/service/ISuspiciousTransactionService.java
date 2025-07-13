package com.thuc.payments.service;

import com.thuc.payments.dto.FilterTranLogsDto;
import com.thuc.payments.dto.PageResponseDto;
import com.thuc.payments.dto.SuspiciousTransactionDto;

import java.text.ParseException;
import java.util.List;

public interface ISuspiciousTransactionService {
    SuspiciousTransactionDto getSuspiciousTransactionById(Integer id);

    List<String> getAllSuspiciousTranTypes();

    PageResponseDto<List<SuspiciousTransactionDto>> getSuspiciousTransByFilter(FilterTranLogsDto filterDto) throws ParseException;

    PageResponseDto<List<SuspiciousTransactionDto>> getSuspiciousTransByKeyword(String keyword, Integer pageNo, Integer pageSize);

    PageResponseDto<List<SuspiciousTransactionDto>> getSuspiciousTransLocked(String keyword , Integer pageNo, Integer pageSize);

    List<Integer> unClock(List<Integer> userIds);
}
