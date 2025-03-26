package com.thuc.rooms.dto;


import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PropertyDto {
    private int id;

    private String name;

    private String propertyType;

    private int ratingStar;

    private String address;

    private BigDecimal latitude;

    private BigDecimal longitude;

    private String overview;

    private List<String> facilities;

    private List <String> images;

    private int numReviews;

    private double avgReviewScore;

    private boolean deleted;
}
