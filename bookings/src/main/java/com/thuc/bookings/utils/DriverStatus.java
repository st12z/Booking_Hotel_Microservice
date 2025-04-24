package com.thuc.bookings.utils;

import lombok.Getter;


@Getter
public enum DriverStatus {
    ACTIVE("ACTIVE"),
    OFFLINE("OFFLINE"),
    BUSY("BUSY");
    private String value;

    DriverStatus(String value) {
        this.value = value;
    }

}
