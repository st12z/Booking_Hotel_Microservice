package com.thuc.payments.repository;

import com.thuc.payments.entity.PaymentTransaction;
import com.thuc.payments.utils.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Integer> {
    PaymentTransaction findByVnpTxnRef(String billCode);

    PaymentTransaction findByVnpTxnRefAndTransactionType(String billCode, TransactionType value);
}
