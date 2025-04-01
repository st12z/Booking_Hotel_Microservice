package com.thuc.rooms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterDto {
    private Integer budget;
    private List<String> distance;
    private List<String> facilities;
    private List<String> propertyType;
    private List<Integer> rating;
    private List<String> reviewScore;
    private Integer quantityBeds;
}
