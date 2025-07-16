package com.thuc.rooms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class TripTypeDto implements Serializable {
    private String tripType;
    private String imageIcon;
    private Integer id;
    private LocalDateTime createdAt;
}
