package com.thuc.rooms.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class TripTypeDto implements Serializable {
    private String tripType;
    private String imageIcon;
}
