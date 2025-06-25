package com.thuc.rooms.repository;

import com.thuc.rooms.entity.Room;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room,Integer> {
    Room findByRoomNumberAndPropertyIdAndRoomTypeId(int roomNumber, int propertyId, int roomTypeId);

    List<Room> findByRoomTypeIdAndPropertyId(Integer id, int propertyId);

    @Query("SELECT r from Room r WHERE r.roomNumber=:roomNumber AND r.roomType.id=:roomTypeId AND r.property.id=:propertyId")
    Room findByRoomNumberAndRoomTypeIdAndPropertyId(@NotNull(message = "roomNumber is null") @Min(value = 100, message = "roomNumber must be at least 100") Integer roomNumber,
                                                    @NotNull(message = "roomTypeId is null") Integer roomTypeId,
                                                    Integer propertyId);

    @Query("SELECT COUNT(*) FROM Room r WHERE r.roomType.id = :roomTypeId AND r.property.id = :propertyId")
    Integer countByPropertyIdAndRoomTypeId(@Param("propertyId") Integer propertyId,
                                           @Param("roomTypeId") Integer roomTypeId);
}
