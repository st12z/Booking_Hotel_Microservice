package com.thuc.bookings.controller;

import com.thuc.bookings.service.IDriversService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/drivers")
@RequiredArgsConstructor
public class DriverControllers {
    private final IDriversService driversService;
    @GetMapping("")
    public ResponseEntity<?> drivers() {
        return ResponseEntity.ok().build();
    }
}
