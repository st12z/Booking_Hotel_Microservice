package com.thuc.payments.utils;

import lombok.Data;
import lombok.Getter;

@Getter
public enum CheckExceed {
    AMOUNT(5),
    FREQUENCY(3);
    private final int value;
    private CheckExceed(int value) {
        this.value = value;
    }
}
