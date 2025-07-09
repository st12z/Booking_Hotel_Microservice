package com.thuc.payments.repository;

import com.thuc.payments.entity.SuspiciousPaymentLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SuspiciousPaymentLogRepository extends JpaRepository<SuspiciousPaymentLog, Integer> {

}
