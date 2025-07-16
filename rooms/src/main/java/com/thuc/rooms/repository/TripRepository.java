package com.thuc.rooms.repository;

import com.thuc.rooms.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip, Integer> {

    List<Trip> findByTripTypeContainingIgnoreCase(String trip);
}
