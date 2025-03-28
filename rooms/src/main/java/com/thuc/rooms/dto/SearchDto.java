package com.thuc.rooms.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SearchDto {
    private String destination;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private int quantityBed;
}
