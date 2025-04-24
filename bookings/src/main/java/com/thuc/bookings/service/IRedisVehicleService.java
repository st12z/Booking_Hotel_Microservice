package com.thuc.bookings.service;

import com.thuc.bookings.dto.requestDto.VehicleRequestDto;

public interface IRedisVehicleService {
    public void saveData(String key, VehicleRequestDto vehicleDto);
    public int getData(String key);
    public void deleteData(String key);
}
