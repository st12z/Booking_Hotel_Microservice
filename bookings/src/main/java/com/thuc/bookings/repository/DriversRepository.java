package com.thuc.bookings.repository;

import com.thuc.bookings.entity.Drivers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriversRepository extends JpaRepository<Drivers,Integer> {
}
