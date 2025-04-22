package com.thuc.bookings.repository;

import com.thuc.bookings.entity.Vehicles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehiclesRepository extends JpaRepository<Vehicles, String> {
}
