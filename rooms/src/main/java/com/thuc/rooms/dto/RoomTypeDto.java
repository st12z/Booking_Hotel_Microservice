package com.thuc.rooms.dto;


import lombok.*;

import java.io.Serializable;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RoomTypeDto implements Serializable {
    private Integer id;

    private Integer propertyId;

    private String name;

    private Integer price;

    private Integer maxGuests;

    private Integer totalRooms;

    private Integer area;

    private Integer discount;

    private Integer numBeds;

    private List<String> freeServices;

    private Boolean status;

}
