package com.thuc.rooms.converter;

import com.thuc.rooms.dto.TripTypeDto;
import com.thuc.rooms.entity.TripType;

public class TripTypeConverter {
    public static TripTypeDto toTripTypeDto(TripType tripType) {
        return TripTypeDto.builder()
                .id(tripType.getId())
                .imageIcon(tripType.getIcon())
                .tripType(tripType.getName())
                .createdAt(tripType.getCreatedAt())
                .build();
    }
}
