package com.thuc.rooms.dto;


import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PropertyDto implements Serializable {
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

    private Double distanceFromCenter;

    private Double distanceFromTrip;

    private String cityName;

    private Integer cityId;

    private List<ReviewDto> reviews;
    private double ratingWifi;
    private double ratingLocation;
    private double ratingFacilities;
    private double ratingClean;
    private double ratingStaff;
    private double ratingComfort;

    private int totalPayments;

    private int totalBills;

}
