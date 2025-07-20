package com.thuc.bookings.service;

import com.thuc.bookings.dto.requestDto.VehicleHoldDto;

public interface IRedisVehicleService {
    public void saveData(String key, VehicleHoldDto vehicleDto);
    public int getData(String key);
    public void deleteData(String key);
}
