package com.thuc.bookings.dto.responseDto;


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

    private Integer area;

    private Integer discount;

    private Integer numBeds;

    private List<String> freeServices;

    private List<RoomDto> rooms;

    private Boolean status;

}
