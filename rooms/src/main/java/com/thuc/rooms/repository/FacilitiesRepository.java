package com.thuc.rooms.repository;

import com.thuc.rooms.entity.Facilities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacilitiesRepository extends JpaRepository<Facilities,Integer> {
}
