package com.thuc.rooms.converter;

import com.thuc.rooms.dto.RoomTypeDto;
import com.thuc.rooms.entity.Facilities;
import com.thuc.rooms.entity.RoomType;

public class RoomTypeConverter {
    public static  RoomTypeDto toRoomTypDto(RoomType roomType){
        return RoomTypeDto.builder()
                .id(roomType.getId())
                .name(roomType.getName())
                .area(roomType.getArea())
                .propertyId(roomType.getProperty().getId())
                .price(roomType.getPrice())
                .discount(roomType.getDiscount())
                .maxGuests(roomType.getMaxGuests())
                .numBeds(roomType.getNumBeds())
                .freeServices(roomType.getFreeServices().stream().map(Facilities::getName).toList())
                .status(roomType.getStatus())
                .build();
    }
}
