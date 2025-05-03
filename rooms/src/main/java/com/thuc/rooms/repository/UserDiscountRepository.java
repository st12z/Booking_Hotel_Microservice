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
    @Query("SELECT u from UserDiscount u where u.email=:email")
    List<UserDiscount> findByEmail(@Param("email") String email);
    @Query("SELECT u from UserDiscount u where u.email=:email and u.discountId=:discountId")
    UserDiscount findByDiscountIdAndEmail(@Param("discountId") int discountId,@Param("email") String email);

}
