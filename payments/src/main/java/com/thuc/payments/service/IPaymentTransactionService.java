package com.thuc.payments.service;

import com.thuc.payments.dto.FilterTransactionDto;
import com.thuc.payments.dto.PageResponseDto;
import com.thuc.payments.dto.PaymentTransactionDto;
import jakarta.servlet.http.HttpServletRequest;

import java.text.ParseException;
import java.util.List;


public interface IPaymentTransactionService {
    void createPayment(String vnpResponseCode, String vnpTxnRef, int vnpAmount, String transactionNo,
                       String transactionDate,String ipAddress);
    PageResponseDto<List<PaymentTransactionDto>> getAllTransactions(FilterTransactionDto filterDto) throws ParseException;

    List<String> getALlTransactionTypes();

    PageResponseDto<List<PaymentTransactionDto>> getSearchTransaction(String keyword, Integer pageNo, Integer pageSize);
}
