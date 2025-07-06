package com.thuc.bookings.repository;

import com.thuc.bookings.entity.RefundBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RefundBillRepository extends JpaRepository<RefundBill, Integer> {
    @Query("SELECT SUM(b.vnp_Amount) FROM RefundBill b where b.createdAt BETWEEN :startOfDay AND :endOfDay")
    Integer getTotalRefundToday(LocalDateTime startOfDay, LocalDateTime endOfDay);

    @Query("SELECT rf from  RefundBill rf WHERE rf.vnp_TxnRef=:vnpTxnRef")
    RefundBill findByVnpTxnRef(@Param("vnpTxnRef")String vnpTxnRef);

    List<RefundBill> findByCreatedAtBetween(LocalDateTime startDay, LocalDateTime endDay);
}
