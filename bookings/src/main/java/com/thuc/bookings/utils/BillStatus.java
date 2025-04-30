package com.thuc.bookings.utils;

import lombok.Getter;

@Getter
public enum PaymentStatus {
    PENDING("PENDING"),
    SUCCESS("SUCCESS"),
    CANCEL("CANCEL");
    private String value;
    private PaymentStatus(String value) {
        this.value = value;
    }
}
