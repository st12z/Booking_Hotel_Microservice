package com.thuc.rooms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingCarsRequestDto {
    private int id;
    private int priceBooking;
}
