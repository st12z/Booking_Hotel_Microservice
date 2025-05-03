package com.thuc.rooms.repository;

import com.thuc.rooms.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room,Integer> {
    Room findByRoomNumberAndPropertyIdAndRoomTypeId(int roomNumber, int propertyId, int roomTypeId);

    List<Room> findByRoomTypeIdAndPropertyId(Integer id, int propertyId);
}
