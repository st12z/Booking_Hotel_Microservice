package com.thuc.payments.service.impl;

import com.thuc.payments.entity.PaymentTransaction;
import com.thuc.payments.repository.PaymentTransactionRepository;
import com.thuc.payments.service.IPaymentTransactionService;
import com.thuc.payments.utils.TransactionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service

@RequiredArgsConstructor
public class PaymentTransactionImpl implements IPaymentTransactionService {
    private final PaymentTransactionRepository paymentTransactionRepository;
    @Override
    public void createPayment(String vnpResponseCode, String vnpTxnRef, int vnpAmount,
                              String vnpTransactionNo, String vnpTransactionDate) {
        PaymentTransaction paymentTransaction =  PaymentTransaction.builder()
                .transactionType(TransactionType.PAYMENT)
                .vnpResponseCode(vnpResponseCode)
                .vnpTxnRef(vnpTxnRef)
                .vnpAmount(vnpAmount/100)
                .vnpTransactionNo(vnpTransactionNo)
                .vnpTransactionDate(vnpTransactionDate)
                .build();
        paymentTransactionRepository.save(paymentTransaction);
    }
}
