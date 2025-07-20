package com.thuc.bookings.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thuc.bookings.constants.VehicleConstant;
import com.thuc.bookings.dto.requestDto.FilterCarAdminDto;
import com.thuc.bookings.dto.requestDto.FilterCarDto;
import com.thuc.bookings.dto.requestDto.VehicleHoldDto;
import com.thuc.bookings.dto.requestDto.VehicleRequestDto;
import com.thuc.bookings.dto.responseDto.BookingDto;
import com.thuc.bookings.dto.responseDto.PageResponseDto;
import com.thuc.bookings.dto.responseDto.SuccessResponseDto;
import com.thuc.bookings.dto.responseDto.VehicleDto;
import com.thuc.bookings.service.IVehiclesService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<SuccessResponseDto<?>> holdVehicle(@RequestBody VehicleHoldDto vehicleDto) {
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
    public ResponseEntity<?> cancelVehicle(@RequestBody VehicleHoldDto vehicleDto) {
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
    @GetMapping("all-cartypes")
    public ResponseEntity<SuccessResponseDto<Map<String,String>>> getAllCarTypes(){
        log.debug("Request to get all car types");
        SuccessResponseDto<Map<String,String>> response = SuccessResponseDto.<Map<String,String>>builder()
                .code(VehicleConstant.STATUS_200)
                .message(VehicleConstant.MESSAGE_200)
                .data(vehiclesService.getAllCarTypes())
                .build();
        return ResponseEntity.ok(response);
    }
    @PostMapping("filter")
    public ResponseEntity<SuccessResponseDto<PageResponseDto<List<VehicleDto>>>> filterVehicle(@RequestBody FilterCarAdminDto filterDto) throws ParseException {
        log.debug("Request to filter vehicle {}", filterDto);
        SuccessResponseDto<PageResponseDto<List<VehicleDto>>> response = SuccessResponseDto.<PageResponseDto<List<VehicleDto>>>builder()
                .code(VehicleConstant.STATUS_200)
                .message(VehicleConstant.MESSAGE_200)
                .data(vehiclesService.getAllVehiclesByFilter(filterDto))
                .build();
        return ResponseEntity.ok(response);
    }
    @GetMapping("search")
    public ResponseEntity<SuccessResponseDto<PageResponseDto<List<VehicleDto>>>> getSearchVehicles(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(required = false,defaultValue = "1") Integer pageNo,
            @RequestParam(required = false,defaultValue = "10") Integer pageSize
    ) throws ParseException {
       log.debug("request get search vehicles with {}", keyword);
        SuccessResponseDto<PageResponseDto<List<VehicleDto>>> response = SuccessResponseDto.<PageResponseDto<List<VehicleDto>>>builder()
                .code(VehicleConstant.STATUS_200)
                .message(VehicleConstant.MESSAGE_200)
                .data(vehiclesService.getSearchVehicles(keyword,pageNo,pageSize))
                .build();
        return ResponseEntity.ok(response);
    }
    @GetMapping("all-car-status")
    public ResponseEntity<SuccessResponseDto<Map<String,String>>> getAllCarStatus(){
        SuccessResponseDto<Map<String,String>> response = SuccessResponseDto.<Map<String,String>>builder()
                .code(VehicleConstant.STATUS_200)
                .message(VehicleConstant.MESSAGE_200)
                .data(vehiclesService.getAllCarStatus())
                .build();
        return ResponseEntity.ok(response);

    }
    @GetMapping("{id}")
    public ResponseEntity<SuccessResponseDto<VehicleDto>> getVehicleById(@PathVariable Integer id) {
        log.debug("Request to get vehicle {}", id);
        SuccessResponseDto<VehicleDto> response = SuccessResponseDto.<VehicleDto>builder()
                .code(VehicleConstant.STATUS_200)
                .message(VehicleConstant.MESSAGE_200)
                .data(vehiclesService.getVehicleById(id))
                .build();
        return ResponseEntity.ok(response);
    }
    @PostMapping("update/{id}")
    public ResponseEntity<SuccessResponseDto<VehicleDto>> updateVehicle(@PathVariable Integer id,
                                           @RequestPart String vehicle,
                                           @RequestPart MultipartFile file
    ) throws JsonProcessingException {
        log.debug("Request to update vehicle {}", vehicle);
        ObjectMapper objectMapper = new ObjectMapper();
        VehicleRequestDto vehicleDto = objectMapper.readValue(vehicle,VehicleRequestDto.class);
        SuccessResponseDto<VehicleDto> response = SuccessResponseDto.<VehicleDto>builder()
                .code(VehicleConstant.STATUS_200)
                .message(VehicleConstant.MESSAGE_200)
                .data(vehiclesService.updateVehicle(id,vehicleDto,file))
                .build();
        return ResponseEntity.ok(response);
    }
    @PostMapping("create")
    public ResponseEntity<SuccessResponseDto<VehicleDto>> createVehicle(@RequestPart String vehicle, @RequestPart MultipartFile file) throws JsonProcessingException {
        log.debug("Request to create vehicle {}", vehicle);
        ObjectMapper objectMapper = new ObjectMapper();
        VehicleRequestDto vehicleDto = objectMapper.readValue(vehicle,VehicleRequestDto.class);
        SuccessResponseDto<VehicleDto> response = SuccessResponseDto.<VehicleDto>builder()
                .code(VehicleConstant.STATUS_200)
                .message(VehicleConstant.MESSAGE_200)
                .data(vehiclesService.createVehicle(vehicleDto,file))
                .build();
        return ResponseEntity.ok(response);
    }

}
