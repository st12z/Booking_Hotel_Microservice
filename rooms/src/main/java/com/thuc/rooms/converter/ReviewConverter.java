package com.thuc.rooms.converter;

import com.thuc.rooms.dto.ReviewDto;
import com.thuc.rooms.entity.Review;

public class ReviewConverter {
    public static Review toReview(ReviewDto reviewDto) {
        return Review.builder()
                .content(reviewDto.getContent())
                .email(reviewDto.getEmail())
                .ratingClean(reviewDto.getRatingClean())
                .ratingComfort(reviewDto.getRatingComfort())
                .ratingFacilities(reviewDto.getRatingFacilities())
                .ratingLocation(reviewDto.getRatingLocation())
                .ratingProperty(reviewDto.getRatingProperty())
                .ratingStaff(reviewDto.getRatingStaff())
                .ratingWifi(reviewDto.getRatingWifi())

                .build();
    }
    public static ReviewDto toReviewDto(Review review) {
        return ReviewDto.builder()
                .id(review.getId())
                .email(review.getEmail())
                .content(review.getContent())
                .ratingClean(review.getRatingClean())
                .ratingComfort(review.getRatingComfort())
                .ratingFacilities(review.getRatingFacilities())
                .ratingLocation(review.getRatingLocation())
                .ratingProperty(review.getRatingProperty())
                .ratingStaff(review.getRatingStaff())
                .ratingWifi(review.getRatingWifi())

                .build();
    }
}
