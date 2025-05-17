package com.thuc.users.repository;

import com.thuc.users.entity.UserVisits;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface UserVisitRepository extends JpaRepository<UserVisits,Integer> {
    @Query("SELECT COUNT(*) FROM UserVisits u where u.accessedAt BETWEEN :startOfDay AND :endOfDay")
    Integer countByAccessedAt(@Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);
}
