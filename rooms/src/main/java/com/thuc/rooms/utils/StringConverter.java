package com.thuc.rooms.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.List;
@Converter(autoApply = true)
public class StringConverter  implements AttributeConverter<List<String>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error converting List<String> to PGobject", e);
        }
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        try {
            if (dbData == null) return null;
            return objectMapper.readValue(dbData.toString(), new TypeReference<List<String>>() {});
        } catch (Exception e) {
            throw new IllegalArgumentException("Error converting PGobject to List<String>", e);
        }
    }
}
