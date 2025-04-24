package com.thuc.bookings.utils;

import lombok.Getter;

@Getter
public enum CarStatus {
    AVAILABLE("AVAILABLE"),
    BUSY("BUSY"),
    INACTIVE("INACTIVE");
    private String value;
    private CarStatus(String value) {
        this.value = value;
    }

}
