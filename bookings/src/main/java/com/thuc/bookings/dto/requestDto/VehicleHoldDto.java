package com.thuc.bookings.dto.requestDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleHoldDto {
    private String email;
    private int vehicleId;
}
