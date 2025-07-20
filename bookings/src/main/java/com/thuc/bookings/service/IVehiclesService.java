package com.thuc.bookings.service;

import com.thuc.bookings.dto.requestDto.FilterCarAdminDto;
import com.thuc.bookings.dto.requestDto.FilterCarDto;
import com.thuc.bookings.dto.requestDto.VehicleHoldDto;
import com.thuc.bookings.dto.requestDto.VehicleRequestDto;
import com.thuc.bookings.dto.responseDto.BookingDto;
import com.thuc.bookings.dto.responseDto.PageResponseDto;
import com.thuc.bookings.dto.responseDto.VehicleDto;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface IVehiclesService {
    List<VehicleDto> getAllVehicles(FilterCarDto filterDto);

    boolean holdVehicle(VehicleHoldDto vehicleDto);

    boolean cancelVehicle(VehicleHoldDto vehicleDto);

    boolean checkVehicle(BookingDto bookingDto);

    Map<String,String> getAllCarTypes();

    PageResponseDto<List<VehicleDto>> getAllVehiclesByFilter(FilterCarAdminDto filterDto) throws ParseException;

    PageResponseDto<List<VehicleDto>> getSearchVehicles(String keyword, Integer pageNo, Integer pageSize);

    VehicleDto getVehicleById(Integer id);

    VehicleDto updateVehicle(Integer id, VehicleRequestDto vehicleDto, MultipartFile file);

    Map<String, String> getAllCarStatus();

    VehicleDto createVehicle(VehicleRequestDto vehicleDto, MultipartFile file);
}
