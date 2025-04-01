package com.thuc.rooms.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SearchDto implements Serializable {
    private String destination;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private Integer quantityBed;
}
