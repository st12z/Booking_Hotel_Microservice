package com.thuc.rooms.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RatingDto {
    private double ratingStaff = 0;
    private double ratingClean = 0;
    private double ratingComfort = 0;
    private double ratingLocation =0;
    private double ratingFacilities = 0;
    private double ratingWifi = 0;
    private double avgReviewScore =0;
    private int ratingStar = 0;

}
