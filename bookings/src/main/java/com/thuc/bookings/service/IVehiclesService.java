package com.thuc.bookings.service;

import com.thuc.bookings.dto.requestDto.FilterDto;
import com.thuc.bookings.dto.requestDto.VehicleRequestDto;
import com.thuc.bookings.dto.responseDto.VehicleDto;

import java.util.List;

public interface IVehiclesService {
    List<VehicleDto> getAllVehicles(FilterDto filterDto);

    boolean holdVehicle(VehicleRequestDto vehicleDto);

    boolean cancelVehicle(VehicleRequestDto vehicleDto);
}
