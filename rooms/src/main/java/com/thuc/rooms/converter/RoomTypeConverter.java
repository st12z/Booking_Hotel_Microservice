package com.thuc.rooms.converter;

import com.thuc.rooms.dto.RoomTypeDto;
import com.thuc.rooms.entity.Facilities;
import com.thuc.rooms.entity.RoomType;

import java.util.stream.Collectors;

public class RoomTypeConverter {
    public static  RoomTypeDto toRoomTypDto(RoomType roomType){
        RoomTypeDto room = RoomTypeDto.builder()
                .id(roomType.getId())
                .name(roomType.getName())
                .area(roomType.getArea())
                .propertyId(roomType.getProperty().getId())
                .price(roomType.getPrice())
                .discount(roomType.getDiscount())
                .maxGuests(roomType.getMaxGuests())
                .numBeds(roomType.getNumBeds())
                .status(roomType.getStatus())
                .build();
        if(roomType.getRooms() != null && !roomType.getRooms().isEmpty()){
            room.setRooms(roomType.getRooms().stream().map(RoomConverter::toRoomDto).collect(Collectors.toList()));
        }
        if(roomType.getFreeServices()!=null){
            room.setFreeServices(roomType.getFreeServices().stream().map(Facilities::getName).collect(Collectors.toList()));
        }
        return room;
    }
}
