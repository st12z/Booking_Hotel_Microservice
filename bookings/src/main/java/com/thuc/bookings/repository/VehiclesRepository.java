package com.thuc.bookings.repository;

import com.thuc.bookings.entity.Vehicles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehiclesRepository extends JpaRepository<Vehicles, Integer> {
    Vehicles findByLicensePlate(String licensePlate);
}
