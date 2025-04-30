package com.thuc.bookings.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingRoomConfirmDto {
    private int roomTypeId;
    private int propertyId;
    private List<Integer> numRooms;
    private int discountHotelId;

    private int discountCarId;
}
