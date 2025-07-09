package com.thuc.bookings.dto.responseDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class BookingRoomConfirmDto implements Serializable {
    private int roomTypeId;
    private int propertyId;

    private String userEmail;

    private List<Integer> numRooms;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime checkIn;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime checkOut;
}
