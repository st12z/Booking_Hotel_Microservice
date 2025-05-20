package com.thuc.rooms.utils;

import com.thuc.rooms.dto.RatingDto;
import com.thuc.rooms.entity.Property;
import com.thuc.rooms.entity.Review;

import java.util.List;

public class CaculateRating {
    public static RatingDto caculateRating(Property property) {
        double ratingStaff = 0;
        double ratingClean = 0;
        double ratingComfort = 0;
        double ratingLocation =0;
        double ratingFacilities = 0;
        double ratingWifi = 0;
        double avgReviewScore =0;
        int ratingStar = 0;
        List<Review> reviews = property.getReviews();
        if(reviews != null && !reviews.isEmpty()) {
            double totalRatingStar = 0;
            double totalRatingStaff = 0;
            double totalRatingClean = 0;
            double totalRatingComfort = 0;
            double totalRatingLocation = 0;
            double totalRatingFacilities = 0;
            double totalRatingWifi = 0;
            for (Review review : reviews) {
                totalRatingStar += review.getRatingProperty();
                totalRatingStaff += review.getRatingStaff();
                totalRatingClean += review.getRatingClean();
                totalRatingComfort += review.getRatingComfort();
                totalRatingLocation += review.getRatingLocation();
                totalRatingFacilities += review.getRatingFacilities();
                totalRatingWifi += review.getRatingWifi();
            }

            int count = reviews.size();


            ratingStaff = totalRatingStaff / count;
            ratingClean = totalRatingClean / count;
            ratingComfort = totalRatingComfort / count;
            ratingLocation = totalRatingLocation / count;
            ratingFacilities = totalRatingFacilities / count;
            ratingWifi = totalRatingWifi / count;
            avgReviewScore =(ratingStar + ratingStaff + ratingClean + ratingComfort + ratingLocation + ratingFacilities + ratingWifi) / 7;
            ratingStar=(int)totalRatingStar / count;
        }
        return new RatingDto(ratingStaff,ratingClean,ratingComfort,ratingLocation,ratingFacilities,ratingWifi,avgReviewScore,ratingStar);
    }
}
