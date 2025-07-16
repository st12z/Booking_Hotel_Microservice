package com.thuc.rooms.converter;

import com.thuc.rooms.dto.PropertyTypeDto;
import com.thuc.rooms.entity.PropertyType;

public class PropertyTypeConverter {
    public static PropertyTypeDto toPropertyTypeDto(PropertyType propertyType) {
        return PropertyTypeDto.builder()
                .id(propertyType.getId())
                .name(propertyType.getName())
                .createdAt(propertyType.getCreatedAt())
                .build();
    }
}
