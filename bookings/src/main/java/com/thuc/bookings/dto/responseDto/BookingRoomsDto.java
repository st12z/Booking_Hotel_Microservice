package com.thuc.bookings.dto.responseDto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingRoomsDto {
    private Integer id;

    private int roomTypeId;

    private int quantityRooms;

    private List<Integer> numRooms;

    private int billId;

    private LocalDateTime checkIn;

    private LocalDateTime checkOut;

    private int dayStays;

    private int originPayment;

    private int promotion;

    private int newPayment;

    private int propertyId;
}
