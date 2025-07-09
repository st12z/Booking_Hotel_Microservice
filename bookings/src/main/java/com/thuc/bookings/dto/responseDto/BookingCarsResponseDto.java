package com.thuc.bookings.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingCarsResponseDto implements Serializable {
    private int id;
    private VehicleDto vehicle;
    private int billId;
    private int priceBooking;
}
