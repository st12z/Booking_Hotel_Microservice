package com.thuc.rooms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingRoomConfirmDto {
    private int roomTypeId;

    private int propertyId;

    private List<Integer> numRooms;

    private int userEmail;

    private int discountHotelId;

    private int discountCarId;



}
