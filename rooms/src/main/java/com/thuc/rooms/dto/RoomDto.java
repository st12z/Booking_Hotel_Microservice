package com.thuc.rooms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RoomDto {
    private Integer id;

    private Integer roomNumber;

    private String status;

    private RoomTypeDto roomType;


}
