package com.thuc.rooms.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RoomRequestDto {

    @NotNull(message = "roomNumber is null")
    @Min(value = 100, message = "roomNumber must be at least 100")
    private Integer roomNumber;

    @NotNull(message = "roomTypeId is null")
    private Integer roomTypeId;

    private Integer propertyId;

    private String status = "available";
}
