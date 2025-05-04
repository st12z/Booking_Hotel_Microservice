package com.thuc.bookings.repository;

import com.thuc.bookings.entity.Bill;
import com.thuc.bookings.utils.BillStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillRepository extends JpaRepository<Bill, Integer> {
    Bill findByBillCode(String billCode);

    Page<Bill> findByUserEmailAndBillStatus(String email, BillStatus billStatus, Pageable pageable);
}
