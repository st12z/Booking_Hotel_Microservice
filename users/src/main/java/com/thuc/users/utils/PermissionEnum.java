package com.thuc.users.utils;

public enum PermissionEnum {
    READ("READ"),
    UPDATE("UPDATE"),
    CREATE("CREATE"),
    DELETE("DELETE");
    private String value;
    private PermissionEnum(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }

}
