package com.thuc.bookings.repository;

import com.thuc.bookings.entity.Bill;
import com.thuc.bookings.utils.BillStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface BillRepository extends JpaRepository<Bill, Integer> {
    Bill findByBillCode(String billCode);

    Page<Bill> findByUserEmailAndBillStatus(String email, BillStatus billStatus, Pageable pageable);

    @Query("SELECT COUNT(*) FROM Bill b where b.createdAt BETWEEN :startOfDay AND :endOfDay")
    Integer countByCreatedAt(@Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);
}
