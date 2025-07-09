package com.thuc.payments.service.impl;

import com.thuc.payments.converter.SuspiciousTransactionConverter;
import com.thuc.payments.dto.SuspiciousTransactionDto;
import com.thuc.payments.entity.SuspiciousPaymentLog;
import com.thuc.payments.exception.ResourceNotFoundException;
import com.thuc.payments.repository.SuspiciousPaymentLogRepository;
import com.thuc.payments.service.ISuspiciousTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SuspiciousTransactionServiceImpl implements ISuspiciousTransactionService {
    private final SuspiciousPaymentLogRepository suspiciousPaymentLogRepository;

    @Override
    public SuspiciousTransactionDto getSuspiciousTransactionById(Integer id) {
        SuspiciousPaymentLog suspiciousPaymentLog = suspiciousPaymentLogRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("SuspiciousPaymentLog", "id", String.valueOf(id)));
        return SuspiciousTransactionConverter.toSuspiciousTransactionDto(suspiciousPaymentLog);
    }
}
