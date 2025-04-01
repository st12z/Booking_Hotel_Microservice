package com.thuc.rooms.converter;


import com.thuc.rooms.dto.PropertyDto;
import com.thuc.rooms.entity.Property;

import java.util.stream.Collectors;

public class PropertyConverter {
    public static PropertyDto toPropertyDto(Property property) {
        return PropertyDto.builder()
                .id(property.getId())
                .name(property.getName())
                .propertyType(property.getPropertyType())
                .avgReviewScore(property.getAvgReviewScore())
                .facilities(property.getFacilities())
                .address(property.getAddress())
                .overview(property.getOverview())
                .images(property.getImages())
                .deleted(property.getDeleted())
                .latitude(property.getLatitude())
                .longitude(property.getLongitude())
                .ratingStar(property.getRatingStar())
                .numReviews(property.getNumReviews())
                .distanceFromCenter(property.getDistanceFromCenter())
                .distanceFromTrip(property.getDistanceFromTrip())
                .slug(property.getSlug())
                .cityName(property.getCity().getName())
                .cityId(property.getCity().getId())
                .roomTypes(property.getRoomTypes().stream().map(RoomTypeConverter::toRoomTypDto).toList())
                .build();
    }
}
