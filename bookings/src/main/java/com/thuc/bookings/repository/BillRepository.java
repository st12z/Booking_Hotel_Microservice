package com.thuc.bookings.repository;

import com.thuc.bookings.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillRepository extends JpaRepository<Bill, Integer> {
    Bill findByBillCode(String billCode);
}
