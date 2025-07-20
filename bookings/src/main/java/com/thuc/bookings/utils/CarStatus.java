package com.thuc.bookings.utils;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum CarStatus {
    AVAILABLE("AVAILABLE"),
    BUSY("BUSY"),
    INACTIVE("INACTIVE");
    private String value;
    private CarStatus(String value) {
        this.value = value;
    }
    public static Map<String,String> getValues() {
        Map<String,String> values = new HashMap<>();
        for (CarStatus carStatus : CarStatus.values()) {
            values.put(carStatus.value, carStatus.toString());
        }
        return values;
    }
}
