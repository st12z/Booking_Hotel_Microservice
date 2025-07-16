package com.thuc.rooms.repository;

import com.thuc.rooms.entity.PropertyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyTypeRepository extends JpaRepository<PropertyType, Integer> {
    PropertyType findByName(String name);
}
