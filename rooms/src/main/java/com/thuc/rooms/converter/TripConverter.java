package com.thuc.rooms.converter;

import com.thuc.rooms.dto.TripDto;
import com.thuc.rooms.entity.Trip;

public class TripConverter {
    public static TripDto toTripDto(Trip trip) {
        return TripDto.builder()
                .id(trip.getId())
                .name(trip.getName())
                .tripType(trip.getTripType())
                .image(trip.getImage())
                .city_id(trip.getCity().getId())
                .longitude(trip.getLongitude())
                .latitude(trip.getLatitude())
                .createdAt(trip.getCreatedAt())
                .build();
    }
}
