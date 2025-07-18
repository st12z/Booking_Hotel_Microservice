package com.thuc.rooms.repository;

import com.thuc.rooms.entity.UserDiscountCars;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDiscountCarsRepository extends JpaRepository<UserDiscountCars,Integer> {
    @Query(value = " SELECT * from user_discount_cars u where u.email=:email " +
            " AND EXISTS (SELECT 1 FROM discount_cars dc WHERE u.discount_car_id = dc.id AND dc.start_date <= now() AND dc.end_date>=now())",nativeQuery = true)
    List<UserDiscountCars> findByEmail(String email);

    @Query(value = "SELECT * FROM user_discount_cars u WHERE u.email=:email AND u.discount_car_id=:discountCarId " +
            " AND EXISTS (SELECT 1 FROM discount_cars dc WHERE u.discount_car_id=dc.id AND dc.start_date <= now() AND dc.end_date>=now())",nativeQuery = true)
    UserDiscountCars findByDiscountCarIdAndEmail(int discountCarId, String email);
}
