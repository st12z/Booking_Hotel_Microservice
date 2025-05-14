package com.thuc.rooms.converter;


import com.thuc.rooms.dto.PropertyDto;
import com.thuc.rooms.entity.Property;
import com.thuc.rooms.entity.Review;
import com.thuc.rooms.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;
@RequiredArgsConstructor
public class PropertyConverter {
    public static PropertyDto toPropertyDto(Property property) {
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

        return PropertyDto.builder()
                .id(property.getId())
                .name(property.getName())
                .propertyType(property.getPropertyType())
                .avgReviewScore(avgReviewScore)
                .facilities(property.getFacilities())
                .address(property.getAddress())
                .overview(property.getOverview())
                .images(property.getImages())
                .deleted(property.getDeleted())
                .latitude(property.getLatitude())
                .longitude(property.getLongitude())
                .ratingStar(ratingStar)
                .numReviews(reviews.size())
                .distanceFromCenter(property.getDistanceFromCenter())
                .distanceFromTrip(property.getDistanceFromTrip())
                .slug(property.getSlug())
                .cityName(property.getCity().getName())
                .cityId(property.getCity().getId())
                .ratingWifi(ratingWifi)
                .ratingClean(ratingClean)
                .ratingComfort(ratingComfort)
                .ratingFacilities(ratingFacilities)
                .ratingLocation(ratingLocation)
                .ratingStaff(ratingStaff)
                .build();
    }
}
