package com.thuc.rooms.repository;

import com.thuc.rooms.entity.DiscountCars;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DiscountCarsRepository extends JpaRepository<DiscountCars, Integer> {
    @Query(" SELECT dc FROM DiscountCars dc WHERE dc.startDate<=CURRENT_TIMESTAMP AND dc.endDate>=CURRENT_TIMESTAMP ")
    List<DiscountCars> findActiveDiscount();

    DiscountCars findByCode(String code);
}
