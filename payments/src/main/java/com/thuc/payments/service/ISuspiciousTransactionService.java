package com.thuc.payments.service;

import com.thuc.payments.dto.SuspiciousTransactionDto;

public interface ISuspiciousTransactionService {
    SuspiciousTransactionDto getSuspiciousTransactionById(Integer id);
}
