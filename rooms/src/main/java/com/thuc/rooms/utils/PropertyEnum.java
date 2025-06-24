package com.thuc.rooms.utils;

import lombok.Getter;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter

public enum PropertyEnum {
    Hotel("Hotel"),
    Apartment("Apartment"),
    Resort("Resort"),
    Villa("Villa"),
    Homestay("Homestay");
    private String propertyName;
    PropertyEnum(String propertyName) {
        this.propertyName = propertyName;
    }
    public static List<String> getProperties() {
        List<String> properties = new ArrayList<String>();
        for (PropertyEnum propertyEnum : PropertyEnum.values()) {
            properties.add(propertyEnum.getPropertyName());
        }
        return properties;
    }
}
