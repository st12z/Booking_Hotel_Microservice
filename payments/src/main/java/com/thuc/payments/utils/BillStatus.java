package com.thuc.payments.utils;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum BillStatus {
    PENDING("PENDING"),
    SUCCESS("SUCCESS"),
    CANCEL("CANCEL");
    private String value;
    private BillStatus(String value) {
        this.value = value;
    }
    public static List<String> getAllValues() {
        List<String> values = new ArrayList<>();
        for (BillStatus status : BillStatus.values()) {
            values.add(status.value);
        }
        return values;
    }
}
