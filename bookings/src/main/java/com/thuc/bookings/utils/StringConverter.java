package com.thuc.bookings.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.List;

@Converter(autoApply = true)
public class StringConverter implements AttributeConverter<List<Integer>,String> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<Integer> attribute) {
        try {

            return objectMapper.writeValueAsString(attribute); // chuyển thành chuỗi JSON
        } catch (Exception e) {
            throw new RuntimeException("Could not convert list of integers to JSON", e);
        }
    }

    @Override
    public List<Integer> convertToEntityAttribute(String dbData) {
        try {
            if (dbData == null || dbData.isEmpty()) {
                return null; // trả về null nếu chuỗi JSON trống
            }
            return objectMapper.readValue(dbData, new TypeReference<List<Integer>>() {}); // Chuyển JSON thành List<Integer>
        } catch (Exception e) {
            throw new RuntimeException("Could not convert JSON to list of integers", e);
        }
    }
}