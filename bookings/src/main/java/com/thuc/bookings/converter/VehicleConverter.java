package com.thuc.bookings.converter;

import com.thuc.bookings.dto.responseDto.VehicleDto;
import com.thuc.bookings.entity.Vehicles;

public class VehicleConverter {
    public static VehicleDto toVehicleDto(Vehicles vehicles) {
        return VehicleDto.builder()
                .id(vehicles.getId())
                .price(vehicles.getPrice())
                .images(vehicles.getImages())
                .licensePlate(vehicles.getLicensePlate())
                .longitude(vehicles.getLongitude())
                .latitude(vehicles.getLatitude())
                .status(vehicles.getStatus().toString())
                .carType(vehicles.getCarType().toString())
                .discount(vehicles.getDiscount())
                .quantity(vehicles.getQuantity())
                .star(vehicles.getStar())
                .createdAt(vehicles.getCreatedAt())
                .build();
    }
}
