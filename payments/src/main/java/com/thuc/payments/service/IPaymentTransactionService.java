package com.thuc.payments.service;

public interface IPaymentTransactionService {
    void createPayment(String vnpResponseCode, String vnpTxnRef, int vnpAmount, String transactionNo, String transactionDate);
}
