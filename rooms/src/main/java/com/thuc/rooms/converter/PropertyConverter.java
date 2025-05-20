package com.thuc.rooms.converter;


import com.thuc.rooms.dto.PropertyDto;
import com.thuc.rooms.dto.RatingDto;
import com.thuc.rooms.entity.Facilities;
import com.thuc.rooms.entity.Property;
import com.thuc.rooms.entity.PropertyImages;
import com.thuc.rooms.entity.Review;
import com.thuc.rooms.repository.ReviewRepository;
import com.thuc.rooms.utils.CaculateRating;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;
@RequiredArgsConstructor
public class PropertyConverter {
    public static PropertyDto toPropertyDto(Property property) {

        RatingDto ratingDto = CaculateRating.caculateRating(property);
        return PropertyDto.builder()
                .id(property.getId())
                .name(property.getName())
                .propertyType(property.getPropertyType())
                .avgReviewScore(ratingDto.getAvgReviewScore())
                .facilities(property.getFacilities().stream().map(Facilities::getName).toList())
                .address(property.getAddress())
                .overview(property.getOverview())
                .images(property.getPropertyImages().stream().map(PropertyImages::getImage).toList())
                .deleted(property.getDeleted())
                .latitude(property.getLatitude())
                .longitude(property.getLongitude())
                .ratingStar(ratingDto.getRatingStar())
                .numReviews(property.getReviews().size())
                .distanceFromCenter(property.getDistanceFromCenter())
                .distanceFromTrip(property.getDistanceFromTrip())
                .slug(property.getSlug())
                .cityName(property.getCity().getName())
                .cityId(property.getCity().getId())
                .ratingWifi(ratingDto.getRatingWifi())
                .ratingClean(ratingDto.getRatingClean())
                .ratingComfort(ratingDto.getRatingComfort())
                .ratingFacilities(ratingDto.getRatingFacilities())
                .ratingLocation(ratingDto.getRatingLocation())
                .ratingStaff(ratingDto.getRatingStaff())
                .build();
    }
}
