package com.thuc.rooms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class RoomTypeRequestDto implements Serializable {
    @NotNull(message = "propertyId is null")
    private Integer propertyId;

    @NotBlank(message = "name is blank")
    private String name;

    @NotNull(message = "price is null")
    private Integer price;

    @NotNull(message = "maxGuests is null")
    private Integer maxGuests;

    @NotNull(message = "area is null")
    private Integer area;

    @NotNull(message = "discount is null")
    private Integer discount;

    @NotNull(message = "numBeds is null")
    private Integer numBeds;

    private List<Integer> freeServices;

    private List<Integer> rooms;
}
