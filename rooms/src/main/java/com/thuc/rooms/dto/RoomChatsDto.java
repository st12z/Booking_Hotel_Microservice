package com.thuc.rooms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomChatsDto {
    private int id;

    private int userAId;

    private int userBId;
}
