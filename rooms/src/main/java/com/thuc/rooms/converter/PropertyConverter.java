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
        PropertyDto propertyDto= PropertyDto.builder()
                .id(property.getId())
                .name(property.getName())
                .propertyType(property.getPropertyType())
                .avgReviewScore(ratingDto.getAvgReviewScore())
                .address(property.getAddress())
                .overview(property.getOverview())
                .images(property.getPropertyImages().stream().map(PropertyImages::getImage).toList())
                .latitude(property.getLatitude())
                .longitude(property.getLongitude())
                .ratingStar(ratingDto.getRatingStar())
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
        if(property.getReviews() != null && !property.getReviews().isEmpty()) {
            propertyDto.setReviews(property.getReviews().stream().map(ReviewConverter::toReviewDto).toList());
            propertyDto.setNumReviews(property.getReviews().size());
        }
        if(property.getRooms() != null && !property.getRooms().isEmpty()) {
            propertyDto.setRooms(property.getRooms().stream().map(RoomConverter::toRoomDto).toList());
        }
        if(property.getFacilities() != null && !property.getFacilities().isEmpty()) {
            propertyDto.setFacilities(property.getFacilities().stream().map(Facilities::getName).toList());
        }
        if(property.getRoomTypes() != null && !property.getRoomTypes().isEmpty()) {
            propertyDto.setRoomTypes(property.getRoomTypes().stream().map(RoomTypeConverter::toRoomTypDto).toList());
        }
        return propertyDto;
    }
}
