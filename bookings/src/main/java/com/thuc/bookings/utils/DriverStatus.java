package com.thuc.bookings.utils;

import lombok.Getter;


@Getter
public enum DriverStatus {
    ACTIVE("active"),
    OFFLINE("offline"),
    BUSY("busy");
    private String value;

    DriverStatus(String value) {
        this.value = value;
    }

}
