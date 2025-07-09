package com.thuc.payments.repository;

import com.thuc.payments.entity.PaymentTransaction;
import com.thuc.payments.utils.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Integer> {
    PaymentTransaction findByVnpTxnRef(String billCode);

    PaymentTransaction findByVnpTxnRefAndTransactionType(String billCode, TransactionType value);

    @Query("SELECT pt FROM PaymentTransaction pt WHERE pt.createdAt >=:startDay AND pt.createdAt<=:endDay AND pt.transactionType =:transactionType")
    List<PaymentTransaction> findByTransactionTypeAndCreatedAtBetween(TransactionType transactionType, LocalDateTime startDay, LocalDateTime endDay);

    List<PaymentTransaction> findByCreatedAtBetween(LocalDateTime startDay, LocalDateTime endDay);

    @Query("SELECT AVG(pt.vnpAmount) FROM PaymentTransaction pt WHERE pt.userId=:userId " +
            "AND pt.vnpResponseCode=:vnpResponseCode AND pt.transactionType=:transactionType")
    Double avgByUserIdAndVnpResponseCodeAndTransactionType(Integer userId, String vnpResponseCode, TransactionType transactionType);
}
