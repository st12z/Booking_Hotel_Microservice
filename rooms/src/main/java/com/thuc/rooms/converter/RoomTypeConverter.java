package com.thuc.rooms.converter;

import com.thuc.rooms.dto.RoomTypeDto;
import com.thuc.rooms.entity.RoomType;

public class RoomTypeConverter {
    public static final RoomTypeDto toRoomTypDto(RoomType roomType){
        return RoomTypeDto.builder()
                .id(roomType.getId())
                .name(roomType.getName())
                .area(roomType.getArea())
                .propertyId(roomType.getProperty().getId())
                .totalRooms(roomType.getTotalRooms())
                .price(roomType.getPrice())
                .discount(roomType.getDiscount())
                .maxGuests(roomType.getMaxGuests())
                .numBeds(roomType.getNumBeds())
                .build();
    }
}
