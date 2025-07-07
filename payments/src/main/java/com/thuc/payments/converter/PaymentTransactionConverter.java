package com.thuc.payments.converter;

import com.thuc.payments.dto.PaymentTransactionDto;
import com.thuc.payments.entity.PaymentTransaction;

public class PaymentTransactionConverter {
    public static PaymentTransactionDto toPaymentTransactionDto(PaymentTransaction paymentTransaction) {
        return PaymentTransactionDto.builder()
                .id(paymentTransaction.getId())
                .transactionType(paymentTransaction.getTransactionType().getValue())
                .vnpTransactionNo(paymentTransaction.getVnpTransactionNo())
                .vnpTransactionDate(paymentTransaction.getVnpTransactionDate())
                .vnpResponseCode(paymentTransaction.getVnpResponseCode())
                .vnpAmount(paymentTransaction.getVnpAmount())
                .vnpTxnRef(paymentTransaction.getVnpTxnRef())
                .createdAt(paymentTransaction.getCreatedAt())
                .build();
    }
}
