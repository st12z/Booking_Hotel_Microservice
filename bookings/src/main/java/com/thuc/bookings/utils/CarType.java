package com.thuc.bookings.utils;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public static Map<String,String> getValues() {
        Map<String,String> values = new HashMap<>();
        for (CarType carType : CarType.values()) {
            values.put(carType.value, carType.toString());
        }
        return values;
    }
}
