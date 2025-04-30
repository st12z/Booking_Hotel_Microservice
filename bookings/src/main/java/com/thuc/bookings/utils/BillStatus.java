package com.thuc.bookings.utils;

import lombok.Getter;

@Getter
public enum BillStatus {
    PENDING("PENDING"),
    SUCCESS("SUCCESS"),
    CANCEL("CANCEL");
    private String value;
    private BillStatus(String value) {
        this.value = value;
    }
}
