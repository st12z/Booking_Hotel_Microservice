package com.thuc.rooms.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.List;
@Converter(autoApply = true)
public class StringConverter implements AttributeConverter<List<String>,String> {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public String convertToDatabaseColumn(List<String> strings) {
        try{
            return objectMapper.writeValueAsString(strings);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> convertToEntityAttribute(String s) {
        try{
            if(s == null || s.isEmpty()){
                return null;
            }
            return objectMapper.readValue(s, new TypeReference<List<String>>(){});
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
