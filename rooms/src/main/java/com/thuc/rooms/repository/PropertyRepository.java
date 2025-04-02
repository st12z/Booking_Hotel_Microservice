package com.thuc.rooms.repository;

import com.thuc.rooms.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property,String> {
    List<Property> findByCityId(int cityId);

    Property findBySlug(String slug);
}
