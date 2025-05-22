package com.thuc.payments.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum TransactionType {
    PAYMENT("PAYMENT"),
    REFUND("REFUND");
    private String value;
    private TransactionType(String value) {
        this.value = value;
    }

}
