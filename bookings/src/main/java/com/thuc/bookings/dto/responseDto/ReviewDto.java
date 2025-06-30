package com.thuc.bookings.dto.responseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewDto {
    private int id;

    private String content;

    private float ratingProperty;

    private float ratingStaff;

    private float ratingFacilities;

    private int userId;

    private float ratingClean;

    private float ratingComfort;

    private float ratingLocation;

    private float ratingWifi;

    List<String> images;

    private int propertyId;

    private LocalDateTime createdAt;
}
