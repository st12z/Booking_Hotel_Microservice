package com.thuc.users.utils;

import lombok.Getter;

@Getter
public enum RoleEnum {
    USER("USER"),
    ADMIN("ADMIN"),
    STAFF("STAFF"),
    MANAGER("MANAGER");
    private String value;
    private RoleEnum(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
