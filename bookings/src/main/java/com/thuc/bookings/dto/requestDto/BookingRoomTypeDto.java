package com.thuc.bookings.dto.requestDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingRoomTypeDto {

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime checkIn;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime checkOut;

    @NotNull(message = "dayStays is not null")
    private int dayStays;

    @NotNull(message = "originPayment is not null")
    private int originPayment;

    @NotNull(message = "newPayment is not null")
    private int newPayment;

    @NotNull(message="promotion is not null")
    private int promotion;

    @NotNull(message ="quantityRooms is not null")
    private int quantityRooms;

    @NotNull(message = "roomTypeId is not null")
    private int roomTypeId;

    private int propertyId;
}
