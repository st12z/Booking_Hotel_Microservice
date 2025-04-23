package com.thuc.rooms.repository;

import com.thuc.rooms.entity.UserDiscountCars;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDiscountCarsRepository extends JpaRepository<UserDiscountCars,Integer> {
    List<UserDiscountCars> findByEmail(String email);
}
