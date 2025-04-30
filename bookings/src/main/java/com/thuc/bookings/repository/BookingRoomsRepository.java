package com.thuc.bookings.repository;

import com.thuc.bookings.entity.BookingRooms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRoomsRepository extends JpaRepository<BookingRooms,Integer> {
    List<BookingRooms> findByBillId(Integer id);
}
