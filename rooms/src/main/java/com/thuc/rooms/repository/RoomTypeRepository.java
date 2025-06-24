package com.thuc.rooms.repository;

import com.thuc.rooms.entity.RoomType;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomTypeRepository extends JpaRepository<RoomType, Integer> {
    List<RoomType> findByPropertyId(int propertyId);

    RoomType findByName(@NotBlank(message = "name is blank") String name);
}
