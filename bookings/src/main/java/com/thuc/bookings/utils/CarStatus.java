package com.thuc.bookings.utils;

import lombok.Getter;

@Getter
public enum CarStatus {
    AVAILABLE("available"),
    BUSY("busy"),
    INACTIVE("inactive");
    private String value;
    private CarStatus(String value) {
        this.value = value;
    }

}
