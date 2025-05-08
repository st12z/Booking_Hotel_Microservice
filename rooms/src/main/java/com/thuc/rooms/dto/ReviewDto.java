package com.thuc.rooms.dto;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

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

    private String email;

    private float ratingClean;

    private float ratingComfort;

    private float ratingLocation;

    private float ratingWifi;

    List<String> images;
}
