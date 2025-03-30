package com.thuc.rooms.dto;


import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RoomTypeDto {
    private Integer id;

    private Integer propertyId;

    private String name;

    private Integer price;

    private Integer maxGuests;

    private Integer totalRooms;

    private Integer area;

    private Integer discount;
}
