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
    private Integer id;

    private String name;

    private String propertyType;

    private Integer ratingStar;

    private String address;

    private BigDecimal latitude;

    private BigDecimal longitude;

    private String overview;

    private List<String> facilities;

    private List <String> images;

    private String slug;

    private Integer numReviews;

    private Double avgReviewScore;

    private Boolean deleted;

    private Double distanceFromCenter;

    private Double distanceFromTrip;
}
