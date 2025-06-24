package com.thuc.rooms.repository;

import com.thuc.rooms.entity.PropertyImages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PropertyImagesRepository extends JpaRepository<PropertyImages,Integer> {
    @Transactional
    @Modifying
    @Query("DELETE FROM PropertyImages p WHERE p.property.id = :propertyId")
    void deleteByPropertyId(@Param("propertyId") int propertyId);
}
