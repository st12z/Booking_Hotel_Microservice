package com.thuc.rooms.repository;

import com.thuc.rooms.entity.Discount;
import com.thuc.rooms.entity.UserDiscount;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDiscountRepository extends JpaRepository<UserDiscount,Integer> {
    @Query(value = " SELECT * from user_discounts u where u.email=:email " +
            " AND EXISTS (SELECT 1 FROM discount d WHERE u.discount_id = d.id AND d.start_date <= now() AND d.end_date>=now())",nativeQuery = true)
    List<UserDiscount> findByEmail(@Param("email") String email);
    @Query(value = "SELECT * FROM user_discounts u WHERE u.email=:email AND u.discount_id=:discountId " +
            " AND EXISTS (SELECT 1 FROM discount d WHERE u.discount_id=d.id AND d.start_date <= now() AND d.end_date>=now())",nativeQuery = true)
    UserDiscount findByDiscountIdAndEmail(@Param("discountId") int discountId,@Param("email") String email);

}
