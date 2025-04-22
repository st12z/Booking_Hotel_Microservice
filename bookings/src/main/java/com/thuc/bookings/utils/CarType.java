package com.thuc.bookings.utils;

import lombok.Getter;

@Getter
public enum CarType {
    BUS("bus"),
    SEAT_7("7-seat"),
    LIMOUSINE("limousine"),
    TAXI("taxi");
    private String value;
    CarType(String value) {
        this.value = value;
    }
}
