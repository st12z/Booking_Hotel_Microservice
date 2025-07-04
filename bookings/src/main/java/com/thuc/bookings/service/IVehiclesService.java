package com.thuc.bookings.service;

import com.thuc.bookings.dto.requestDto.BookingDto;
import com.thuc.bookings.dto.requestDto.FilterCarDto;
import com.thuc.bookings.dto.requestDto.VehicleRequestDto;
import com.thuc.bookings.dto.responseDto.VehicleDto;

import java.util.List;

public interface IVehiclesService {
    List<VehicleDto> getAllVehicles(FilterCarDto filterDto);

    boolean holdVehicle(VehicleRequestDto vehicleDto);

    boolean cancelVehicle(VehicleRequestDto vehicleDto);

    boolean checkVehicle(BookingDto bookingDto);
}
