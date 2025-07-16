package com.thuc.rooms.repository;

import com.thuc.rooms.entity.TripType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripTypeRepository extends JpaRepository<TripType,Integer> {
}
