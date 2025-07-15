package com.thuc.rooms.converter;

import com.thuc.rooms.dto.CityDto;
import com.thuc.rooms.dto.PropertyDto;
import com.thuc.rooms.dto.TripDto;
import com.thuc.rooms.entity.City;
import com.thuc.rooms.entity.Property;

import java.util.List;
import java.util.stream.Collectors;

public class CityConverter {
    public static CityDto toCityDto(City city){
        return CityDto.builder()
                .id(city.getId())
                .name(city.getName())
                .image(city.getImage())
                .slug(city.getSlug())
                .createdAt(city.getCreatedAt())
                .build();
    }
}
