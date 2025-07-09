package com.thuc.payments.converter;

import com.thuc.payments.dto.SuspiciousTransactionDto;
import com.thuc.payments.entity.SuspiciousPaymentLog;

public class SuspiciousTransactionConverter {
    public static SuspiciousTransactionDto toSuspiciousTransactionDto(SuspiciousPaymentLog suspiciousPaymentLog) {
        return SuspiciousTransactionDto.builder()
                .id(suspiciousPaymentLog.getId())
                .amount(suspiciousPaymentLog.getAmount())
                .suspiciousReason(suspiciousPaymentLog.getSuspiciousReason())
                .suspiciousType(suspiciousPaymentLog.getSuspiciousType())
                .billCode(suspiciousPaymentLog.getBillCode())
                .build();
    }
}
