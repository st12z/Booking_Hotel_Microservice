package com.thuc.bookings.dto.requestDto;

import lombok.Data;

@Data
public class VehicleRequestDto {
    private String licensePlate;
    private String carType;
    private Integer price;
    private Integer quantity;
    private Integer discount;
    private Integer star;
    private String status;
}
