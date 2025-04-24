package com.thuc.bookings.repository;

import com.thuc.bookings.entity.Vehicles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehiclesRepository extends JpaRepository<Vehicles, Integer> {
}
