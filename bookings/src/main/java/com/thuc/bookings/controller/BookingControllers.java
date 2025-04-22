package com.thuc.bookings.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class BookingControllers {
    @GetMapping
    public String home() {
        return "Hello World";
    }
}
