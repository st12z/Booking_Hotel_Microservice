package com.thuc.payments.utils;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
@Getter
public enum SuspiciousTypeEnum {
    AMOUNT("AMOUNT"),
    FREQUENCY("FREQUENCY");
    private String value;
    private SuspiciousTypeEnum(String value) {
        this.value = value;
    }
    public static List<String> getAllValues() {
        List<String> values = new ArrayList<String>();
        for (SuspiciousTypeEnum e : SuspiciousTypeEnum.values()) {
            values.add(e.value);
        }
        return values;
    }
}
