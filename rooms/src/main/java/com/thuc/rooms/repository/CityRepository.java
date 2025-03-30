package com.thuc.rooms.repository;

import com.thuc.rooms.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

    Optional<City> findBySlug(String slug);

    City findByNameContainingIgnoreCase(String destination);
}
