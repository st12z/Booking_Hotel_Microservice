package com.thuc.bookings.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RoomDto implements Serializable {
    private Integer id;

    private Integer roomNumber;

    private String status;

    private Integer roomTypeId;

}
