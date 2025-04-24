package com.thuc.bookings.controller;

import com.thuc.bookings.constants.VehicleConstant;
import com.thuc.bookings.dto.requestDto.FilterDto;
import com.thuc.bookings.dto.requestDto.VehicleRequestDto;
import com.thuc.bookings.dto.responseDto.SuccessResponseDto;
import com.thuc.bookings.dto.responseDto.VehicleDto;
import com.thuc.bookings.service.IVehiclesService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleControllers {
    private final IVehiclesService vehiclesService;
    private final Logger log = LoggerFactory.getLogger(VehicleControllers.class);
    @PostMapping("")
    public ResponseEntity<?> getAllVehicles(@RequestBody FilterDto filterDto) {
        log.debug("Request to get all Vehicles with {}", filterDto);
        SuccessResponseDto response = SuccessResponseDto.builder()
                .code(VehicleConstant.STATUS_200)
                .message(VehicleConstant.MESSAGE_200)
                .data(vehiclesService.getAllVehicles(filterDto))
                .build();
        return ResponseEntity.ok(response);
    }
    @PostMapping("hold")
    public ResponseEntity<?> holdVehicle(@RequestBody VehicleRequestDto vehicleDto) {
        log.debug("Request to hold vehicle {}", vehicleDto);
        boolean check = vehiclesService.holdVehicle(vehicleDto);
        String message = check ? "Bạn đã giữ chỗ thành công trong 10p!" : "Xe đã hết chỗ hoặc đang không hoạt động!";
        SuccessResponseDto response = SuccessResponseDto.builder()
                .code(VehicleConstant.STATUS_200)
                .message(VehicleConstant.MESSAGE_200)
                .data(message)
                .build();
        return ResponseEntity.ok(response);
    }
    @PostMapping("cancel")
    public ResponseEntity<?> cancelVehicle(@RequestBody VehicleRequestDto vehicleDto) {
        log.debug("Request to cancel vehicle {}", vehicleDto);
        boolean check = vehiclesService.cancelVehicle(vehicleDto);
        String message = check ? "Bạn đã hủy đặt xe thành công!" : "Bạn đã hủy đặt xe thất bại!";
        SuccessResponseDto response = SuccessResponseDto.builder()
                .code(VehicleConstant.STATUS_200)
                .message(VehicleConstant.MESSAGE_200)
                .data(message)
                .build();
        return ResponseEntity.ok(response);
    }
}
