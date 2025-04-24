package com.thuc.bookings.utils;

import lombok.Getter;

@Getter
public enum CarType {
    BUS("Bus"),
    SEAT_7("Xe 7 chỗ"),
    SEAT_4("Xe 4 chỗ"),
    LIMOUSINE("Limousine"),
    TAXI("Taxi");
    private String value;
    CarType(String value) {
        this.value = value;
    }
}
