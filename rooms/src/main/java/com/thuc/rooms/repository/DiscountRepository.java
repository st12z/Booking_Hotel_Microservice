package com.thuc.rooms.repository;

import com.thuc.rooms.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Integer> {
    @Query(" SELECT d FROM Discount d WHERE d.startDate<=CURRENT_TIMESTAMP AND d.endDate>=CURRENT_TIMESTAMP ")
    List<Discount> findActiveDiscount();
}
