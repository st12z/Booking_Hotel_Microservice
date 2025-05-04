package com.thuc.bookings.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingCarsResponseDto {
    private int id;
    private VehicleDto vehicle;
    private int billId;
    private int priceBooking;
}
