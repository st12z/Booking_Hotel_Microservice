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
import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Integer> {
    Bill findByBillCode(String billCode);


    @Query("SELECT COUNT(*) FROM Bill b where b.createdAt BETWEEN :startOfDay AND :endOfDay AND b.billStatus=:billStatus")
    Integer countByCreatedAtAndBillStatus(
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay,
            @Param("billStatus") BillStatus billStatus
    );

    @Query("SELECT SUM(b.newTotalPayment) FROM Bill b where b.createdAt BETWEEN :startOfDay AND :endOfDay")
    Integer getTotalPaymentToday(@Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);

    @Query(value = "SELECT * FROM bill b WHERE b.user_email =:email AND ( " +
            " b.bill_code LIKE :keyword " +
            " OR b.phone_number LIKE :keyword " +
            " OR unaccent(b.first_name) ILIKE unaccent(:keyword) " +
            " OR unaccent(b.last_name) ILIKE unaccent(:keyword) " +
            " OR b.email ILIKE (:keyword) " +
            " OR EXISTS(SELECT 1 FROM properties p  WHERE p.id=b.property_id AND unaccent(p.name) ILIKE unaccent(:keyword)))" +
            " ORDER BY created_at desc",
            countQuery = "SELECT COUNT(*) FROM bill b WHERE b.user_email =:email AND ( " +
                    " b.bill_code LIKE :keyword " +
                    " OR b.phone_number LIKE :keyword " +
                    " OR unaccent(b.first_name) ILIKE unaccent(:keyword) " +
                    " OR unaccent(b.last_name) ILIKE unaccent(:keyword) " +
                    " OR b.email ILIKE (:keyword) " +
                    " OR EXISTS(SELECT 1 FROM properties p  WHERE p.id=b.property_id AND unaccent(p.name) ILIKE unaccent(:keyword)))"
            , nativeQuery = true)

    Page<Bill> findByKeyword(String email, String keyword, Pageable pageable);

    Integer countByPropertyId(Integer propertyId);

    List<Bill> findByPropertyId(Integer propertyId);

}