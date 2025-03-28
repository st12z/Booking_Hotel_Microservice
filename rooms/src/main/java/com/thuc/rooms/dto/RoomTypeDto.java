package com.thuc.rooms.dto;


import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RoomTypeDto {
    private int id;

    private int propertyId;

    private String name;

    private int price;

    private int maxGuests;

    private int totalRooms;

    private int area;

    private int discount;
}
