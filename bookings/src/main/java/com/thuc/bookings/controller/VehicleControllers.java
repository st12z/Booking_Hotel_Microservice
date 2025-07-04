package com.thuc.bookings.controller;

import com.thuc.bookings.constants.VehicleConstant;
import com.thuc.bookings.dto.requestDto.BookingDto;
import com.thuc.bookings.dto.requestDto.FilterCarDto;
import com.thuc.bookings.dto.requestDto.VehicleRequestDto;
import com.thuc.bookings.dto.responseDto.SuccessResponseDto;
import com.thuc.bookings.dto.responseDto.VehicleDto;
import com.thuc.bookings.service.IVehiclesService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleControllers {
    private final IVehiclesService vehiclesService;
    private final Logger log = LoggerFactory.getLogger(VehicleControllers.class);
    @PostMapping("")
    public ResponseEntity<SuccessResponseDto<List<VehicleDto>>> getAllVehicles(@RequestBody FilterCarDto filterDto) {
        log.debug("Request to get all Vehicles with {}", filterDto);
        SuccessResponseDto<List<VehicleDto>> response = SuccessResponseDto.<List<VehicleDto>>builder()
                .code(VehicleConstant.STATUS_200)
                .message(VehicleConstant.MESSAGE_200)
                .data(vehiclesService.getAllVehicles(filterDto))
                .build();
        return ResponseEntity.ok(response);
    }
    @PostMapping("hold")
    public ResponseEntity<SuccessResponseDto<?>> holdVehicle(@RequestBody VehicleRequestDto vehicleDto) {
        log.debug("Request to hold vehicle {}", vehicleDto);
        boolean check = vehiclesService.holdVehicle(vehicleDto);
        String message = check ? "Bạn đã giữ chỗ thành công trong 10p!" : "Xe đã hết chỗ hoặc đang không hoạt động!";
        int code = check ? VehicleConstant.STATUS_200 : VehicleConstant.STATUS_400;
        HttpStatus status = check ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        SuccessResponseDto<?> response = SuccessResponseDto.builder()
                .code(code)
                .message(message)
                .build();
        return ResponseEntity.status(status).body(response);
    }
    @PostMapping("cancel")
    public ResponseEntity<?> cancelVehicle(@RequestBody VehicleRequestDto vehicleDto) {
        log.debug("Request to cancel vehicle {}", vehicleDto);
        boolean check = vehiclesService.cancelVehicle(vehicleDto);
        String message = check ? "Bạn đã hủy đặt xe thành công!" : "Bạn đã hủy đặt xe thất bại!";
        SuccessResponseDto<?> response = SuccessResponseDto.builder()
                .code(VehicleConstant.STATUS_200)
                .message(VehicleConstant.MESSAGE_200)
                .data(message)
                .build();
        return ResponseEntity.ok(response);
    }
    @PostMapping("check")
    public ResponseEntity<?> checkVehicle(@RequestBody BookingDto bookingDto) {
        log.debug("Request to check vehicle {}", bookingDto);
        boolean check = vehiclesService.checkVehicle(bookingDto);
        String message = check ? "Bạn có thể đặt xe!":"Vui lòng đặt xe lại!";
        int code = check ? VehicleConstant.STATUS_200 : VehicleConstant.STATUS_400;
        HttpStatus status = check ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        SuccessResponseDto<?> response = SuccessResponseDto.builder()
                .code(code)
                .message(message)
                .build();
        return ResponseEntity.status(status).body(response);

    }
}
