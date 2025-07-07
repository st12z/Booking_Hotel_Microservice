package com.thuc.payments.utils;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum TransactionType {
    PAYMENT("PAYMENT"),
    REFUND("REFUND");
    private String value;
    private TransactionType(String value) {
        this.value = value;
    }
    public static List<String> getAllValues() {
        List<String> values = new ArrayList<>();
        for (TransactionType transactionType : TransactionType.values()) {
            values.add(transactionType.value);
        }
        return values;
    }

}
