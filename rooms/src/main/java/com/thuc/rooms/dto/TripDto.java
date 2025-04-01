package com.thuc.rooms.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TripDto implements Serializable {
    private Integer id;
    private String name;
    private String tripType;
    private Integer city_id;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String image;
}
